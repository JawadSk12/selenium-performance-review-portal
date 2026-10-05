package com.review.portal.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected static final String BASE_URL = "http://localhost:8081";

    private static final String SCREENSHOT_DIR = "screenshots/failures";

    @BeforeEach
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);

        wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    @AfterEach
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }

    protected void openLoginPage() {

        driver.get(BASE_URL + "/login");
    }

    protected void takeScreenshot(String testName) {

        try {

            Path directory = Paths.get(SCREENSHOT_DIR);

            Files.createDirectories(directory);

            String timestamp =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "yyyyMMdd_HHmmss"
                                    )
                            );

            String fileName =
                    testName + "_" + timestamp + ".png";

            File screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.FILE);

            Path destination =
                    directory.resolve(fileName);

            Files.copy(
                    screenshot.toPath(),
                    destination
            );

            System.out.println(
                    "Failure screenshot saved: "
                            + destination.toAbsolutePath()
            );

        } catch (IOException e) {

            System.err.println(
                    "Could not save failure screenshot: "
                            + e.getMessage()
            );
        }
    }
}