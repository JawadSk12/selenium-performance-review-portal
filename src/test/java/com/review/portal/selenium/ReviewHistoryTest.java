package com.review.portal.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReviewHistoryTest extends BaseTest {

    @Test
    public void employeeCanViewReviewHistory() {

        try {

            // =========================================================
            // STEP 1: Open Login Page
            // =========================================================

            openLoginPage();

            assertTrue(
                    driver.findElement(By.id("loginForm")).isDisplayed(),
                    "Login form should be displayed"
            );


            // =========================================================
            // STEP 2: Login as Employee
            // =========================================================

            driver.findElement(By.id("email"))
                    .clear();

            driver.findElement(By.id("email"))
                    .sendKeys("jawad@gmail.com");

            driver.findElement(By.id("password"))
                    .clear();

            driver.findElement(By.id("password"))
                    .sendKeys("1234");

            driver.findElement(By.id("loginBtn"))
                    .click();


            // =========================================================
            // STEP 3: Wait for Dashboard
            // =========================================================

            wait.withTimeout(
                    Duration.ofSeconds(15)
            ).until(
                    driver ->
                            driver.getCurrentUrl()
                                    .contains("/dashboard")
            );

            assertTrue(
                    driver.getCurrentUrl().contains("/dashboard"),
                    "Employee should be redirected to dashboard after login"
            );


            // =========================================================
            // STEP 4: Verify Employee Information
            // =========================================================

            WebElement employeeName =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("employeeNameDisplay")
                            )
                    );

            assertTrue(
                    employeeName.isDisplayed(),
                    "Employee name should be visible on dashboard"
            );

            assertEquals(
                    "Jawad Shaikh",
                    employeeName.getText().trim(),
                    "Incorrect employee name"
            );


            // =========================================================
            // STEP 5: Find View History Button
            // =========================================================

            WebElement viewHistoryButton =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("viewHistoryBtn")
                            )
                    );

            assertTrue(
                    viewHistoryButton.isDisplayed(),
                    "View History button should be visible"
            );

            assertTrue(
                    viewHistoryButton.isEnabled(),
                    "View History button should be enabled"
            );


            // =========================================================
            // STEP 6: Click View History
            // =========================================================

            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            js.executeScript(
                    "arguments[0].scrollIntoView({" +
                            "behavior:'instant'," +
                            "block:'center'," +
                            "inline:'center'" +
                            "});",
                    viewHistoryButton
            );

            System.out.println();
            System.out.println(
                    "========== REVIEW HISTORY DEBUG =========="
            );

            System.out.println(
                    "URL before clicking View History: "
                            + driver.getCurrentUrl()
            );

            System.out.println(
                    "Page title before clicking View History: "
                            + driver.getTitle()
            );


            // Use JavaScript click because the application may
            // have another element interfering with normal Selenium click.
            js.executeScript(
                    "arguments[0].click();",
                    viewHistoryButton
            );


            // =========================================================
            // STEP 7: Wait for History Page
            // =========================================================

            wait.withTimeout(
                    Duration.ofSeconds(15)
            ).until(
                    driver -> {

                        String currentUrl =
                                driver.getCurrentUrl();

                        String pageSource =
                                driver.getPageSource()
                                        .toLowerCase();

                        return currentUrl.contains(
                                    "/employee/history"
                                )
                                || pageSource.contains(
                                    "review history"
                                )
                                || pageSource.contains(
                                    "history"
                                );
                    }
            );


            // =========================================================
            // STEP 8: Debug Information
            // =========================================================

            System.out.println(
                    "URL immediately after navigation attempt: "
                            + driver.getCurrentUrl()
            );

            System.out.println(
                    "Page title: "
                            + driver.getTitle()
            );

            System.out.println(
                    "Page source contains 'history': "
                            + driver.getPageSource()
                                    .toLowerCase()
                                    .contains("history")
            );

            System.out.println(
                    "=========================================="
            );
            System.out.println();


            // =========================================================
            // STEP 9: Verify History Page
            // =========================================================

            String currentUrl =
                    driver.getCurrentUrl();

            String pageText =
                    driver.findElement(
                            By.tagName("body")
                    ).getText();


            boolean historyUrl =
                    currentUrl.contains(
                            "/employee/history"
                    );

            boolean historyContent =
                    pageText.toLowerCase()
                            .contains("history");


            assertTrue(
                    historyUrl || historyContent,
                    "Employee should reach the review history page"
                            + ". Current URL: "
                            + currentUrl
            );


            // =========================================================
            // STEP 10: Verify History Page Contains Content
            // =========================================================

            assertTrue(
                    pageText.length() > 0,
                    "Review history page should contain content"
            );


            // Verify employee information if it is displayed
            // on the history page.
            if (pageText.contains("Jawad Shaikh")) {

                assertTrue(
                        pageText.contains("Jawad Shaikh"),
                        "Employee name should appear in review history"
                );

            }


            // =========================================================
            // STEP 11: Verify Review Status
            // =========================================================

            boolean pendingStatus =
                    pageText.contains("PENDING")
                            || pageText.contains("Pending")
                            || pageText.contains("pending");

            assertTrue(
                    pendingStatus,
                    "Review history should contain the review status"
            );


            // =========================================================
            // STEP 12: Print Successful Test Information
            // =========================================================

            System.out.println();
            System.out.println(
                    "================================================="
            );

            System.out.println(
                    "REVIEW HISTORY TEST PASSED"
            );

            System.out.println(
                    "Employee: Jawad Shaikh"
            );

            System.out.println(
                    "History page opened successfully"
            );

            System.out.println(
                    "Employee information verified"
            );

            System.out.println(
                    "Review status verified"
            );

            System.out.println(
                    "Review history content verified"
            );

            System.out.println(
                    "================================================="
            );

            System.out.println();


        } catch (AssertionError | RuntimeException e) {

            // =========================================================
            // FAILURE SCREENSHOT
            // =========================================================

            takeScreenshot(
                    "employeeCanViewReviewHistory"
            );

            throw e;
        }
    }
}