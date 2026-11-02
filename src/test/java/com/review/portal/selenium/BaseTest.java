package com.review.portal.selenium;

import com.review.portal.PerformanceReviewPortalApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Shared base class for all Selenium browser integration tests.
 *
 * <p>Lifecycle Management:
 * <ol>
 *   <li>{@code @BeforeAll ensureServerRunning} checks if the application is reachable at {@code BASE_URL}.
 *       If already running (e.g., via Tomcat in Jenkins Stage 3 or 'spring-boot:start' in Maven Failsafe),
 *       it reuses the running instance. If not running, it verifies PostgreSQL connectivity and starts
 *       an embedded Spring Boot instance on port 8081 automatically.</li>
 *   <li>{@code @BeforeEach setUp} initialises a headless ChromeDriver session with explicit waits.</li>
 *   <li>{@code @AfterEach tearDown} quits the WebDriver session cleanly to prevent orphaned processes.</li>
 *   <li>{@code @AfterAll stopEmbeddedServer} gracefully shuts down the embedded instance if started by tests.</li>
 * </ol>
 *
 * <p>Configuration:
 * <ul>
 *   <li>Base URL is configurable via {@code -Dselenium.base.url=...} (defaults to {@code http://localhost:8081}).</li>
 *   <li>Failure screenshots are saved to {@code screenshots/failures/}.</li>
 * </ul>
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Base URL for all Selenium tests.
     * Configurable via system property 'selenium.base.url' or environment variable 'TEST_SERVER_URL'.
     */
    protected static final String BASE_URL =
            System.getProperty("selenium.base.url",
                    System.getenv().getOrDefault("TEST_SERVER_URL", "http://localhost:8081"));

    private static final String SCREENSHOT_DIR = "screenshots/failures";
    private static final int APP_READINESS_TIMEOUT_SECONDS = 90;

    /** Holds the in-process Spring Boot application context if launched by BaseTest */
    private static ConfigurableApplicationContext inProcessApp;
    private static boolean launchedByTest = false;

    @BeforeAll
    public static synchronized void ensureServerRunning() {
        if (isApplicationReachable()) {
            System.out.println("[BaseTest] Detected active application at " + BASE_URL + ". Using running instance.");
            return;
        }

        // Verify PostgreSQL is up before attempting to boot the application
        verifyPostgreSqlAvailable();

        System.out.println("[BaseTest] No running server found at " + BASE_URL
                + ". Launching embedded Spring Boot instance on port 8081...");

        try {
            SpringApplication app = new SpringApplication(PerformanceReviewPortalApplication.class);
            // Suppress duplicate banner to keep test logs clean
            app.setBanner((environment, sourceClass, out) -> {});
            inProcessApp = app.run("--server.port=8081");
            launchedByTest = true;

            // Register JVM shutdown hook for clean shutdown if JUnit exits abruptly
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (inProcessApp != null && inProcessApp.isActive()) {
                    System.out.println("[BaseTest] JVM shutdown hook: stopping in-process Spring Boot server...");
                    inProcessApp.close();
                }
            }));

        } catch (Exception e) {
            throw new IllegalStateException("[BaseTest] Failed to launch Spring Boot test server: " + e.getMessage(), e);
        }

        waitForApplicationReady();
    }

    @AfterAll
    public static synchronized void stopEmbeddedServer() {
        // We leave the server running across test classes in the same JVM to speed up the suite,
        // and let the shutdown hook clean it up, or if explicitly needed:
    }

    @BeforeEach
    public void setUp() {
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        // Initialise WebDriver
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("[BaseTest] Error during WebDriver tearDown: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper Methods
    // ─────────────────────────────────────────────────────────────────────────

    protected void openLoginPage() {
        driver.get(BASE_URL + "/login");
    }

    protected void takeScreenshot(String testName) {
        try {
            Path directory = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(directory);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destination = directory.resolve(fileName);
            Files.copy(screenshot.toPath(), destination);

            System.out.println("[BaseTest] Failure screenshot captured: " + destination.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("[BaseTest] Could not save screenshot: " + e.getMessage());
        }
    }

    private static boolean isApplicationReachable() {
        String healthUrl = BASE_URL + "/login";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(healthUrl).openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            conn.setRequestMethod("GET");
            conn.connect();
            int code = conn.getResponseCode();
            conn.disconnect();
            return (code >= 200 && code < 400);
        } catch (IOException e) {
            return false;
        }
    }

    private static void verifyPostgreSqlAvailable() {
        String dbHost = System.getenv().getOrDefault("DB_HOST", "localhost");
        int dbPort = Integer.parseInt(System.getenv().getOrDefault("DB_PORT", "5432"));
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(dbHost, dbPort), 3000);
            System.out.println("[BaseTest] PostgreSQL database connection verified on " + dbHost + ":" + dbPort);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "[BaseTest] PostgreSQL is not reachable at " + dbHost + ":" + dbPort + ". "
                    + "Please ensure your PostgreSQL database service is started and running before running tests.", e);
        }
    }

    private static void waitForApplicationReady() {
        String healthUrl = BASE_URL + "/login";
        long deadline = System.currentTimeMillis() + (long) APP_READINESS_TIMEOUT_SECONDS * 1000;
        System.out.println("[BaseTest] Waiting for application to respond at " + healthUrl + " ...");

        while (System.currentTimeMillis() < deadline) {
            if (isApplicationReachable()) {
                System.out.println("[BaseTest] Application is ready and accepting requests.");
                return;
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("[BaseTest] Interrupted while waiting for application startup.", ie);
            }
        }

        throw new IllegalStateException(
                "[BaseTest] Application at " + healthUrl + " failed to respond within "
                + APP_READINESS_TIMEOUT_SECONDS + " seconds. "
                + "Check application startup logs for JPA, database, or port binding errors.");
    }
}
