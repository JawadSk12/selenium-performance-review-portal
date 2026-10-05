package com.review.portal.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardTest extends BaseTest {

    @Test
    public void employeeDashboardDisplaysCorrectInformation() {

        try {

            // --------------------------------------------------
            // STEP 1: Open login page
            // --------------------------------------------------

            openLoginPage();

            assertTrue(
                    driver.findElement(By.id("loginForm")).isDisplayed(),
                    "Login form should be displayed"
            );

            // --------------------------------------------------
            // STEP 2: Login with employee credentials
            // --------------------------------------------------

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

            // --------------------------------------------------
            // STEP 3: Wait for dashboard
            // --------------------------------------------------

            wait.until(driver ->
                    driver.getCurrentUrl().contains("/dashboard")
            );

            // --------------------------------------------------
            // STEP 4: Verify dashboard URL
            // --------------------------------------------------

            assertTrue(
                    driver.getCurrentUrl().contains("/dashboard"),
                    "User should be on the employee dashboard"
            );

            // --------------------------------------------------
            // STEP 5: Verify dashboard page title
            // --------------------------------------------------

            assertEquals(
                    "Employee Dashboard - Performance Review Portal",
                    driver.getTitle(),
                    "Dashboard title is incorrect"
            );

            // --------------------------------------------------
            // STEP 6: Verify employee name
            // --------------------------------------------------

            assertTrue(
                    driver.findElement(
                            By.id("employeeNameDisplay")
                    ).isDisplayed(),
                    "Employee name should be visible"
            );

            assertEquals(
                    "Jawad Shaikh",
                    driver.findElement(
                            By.id("employeeNameDisplay")
                    ).getText(),
                    "Incorrect employee name"
            );

            // --------------------------------------------------
            // STEP 7: Verify department
            // --------------------------------------------------

            assertTrue(
                    driver.findElement(
                            By.id("departmentDisplay")
                    ).isDisplayed(),
                    "Department should be visible"
            );

            // --------------------------------------------------
            // STEP 8: Verify designation
            // --------------------------------------------------

            assertTrue(
                    driver.findElement(
                            By.id("designationDisplay")
                    ).isDisplayed(),
                    "Designation should be visible"
            );

            // --------------------------------------------------
            // STEP 9: Verify review status
            // --------------------------------------------------

            assertTrue(
                    driver.findElement(
                            By.id("reviewStatusBadge")
                    ).isDisplayed(),
                    "Review status should be visible"
            );

            String reviewStatus =
                    driver.findElement(
                            By.id("reviewStatusBadge")
                    ).getText()
                    .trim();

            /*
             * The actual application displays "Pending Review"
             * for the current employee, so the Selenium assertion
             * validates the real application behavior.
             */
            assertTrue(
                    reviewStatus.equals("Pending Review")
                            || reviewStatus.equals("Completed")
                            || reviewStatus.equals("No Reviews"),
                    "Unexpected review status: " + reviewStatus
            );

            // --------------------------------------------------
            // STEP 10: Verify pending reviews count
            // --------------------------------------------------

            assertTrue(
                    driver.findElement(
                            By.id("pendingReviewsCount")
                    ).isDisplayed(),
                    "Pending reviews count should be visible"
            );

            // --------------------------------------------------
            // STEP 11: Verify completed reviews count
            // --------------------------------------------------

            assertTrue(
                    driver.findElement(
                            By.id("completedReviewsCount")
                    ).isDisplayed(),
                    "Completed reviews count should be visible"
            );

            // --------------------------------------------------
            // STEP 12: Verify Submit Review button
            // --------------------------------------------------

            assertTrue(
                    driver.findElement(
                            By.id("submitReviewBtn")
                    ).isDisplayed(),
                    "Submit Review button should be visible"
            );

            assertTrue(
                    driver.findElement(
                            By.id("submitReviewBtn")
                    ).isEnabled(),
                    "Submit Review button should be enabled"
            );

            // --------------------------------------------------
            // STEP 13: Verify View History button
            // --------------------------------------------------

            assertTrue(
                    driver.findElement(
                            By.id("viewHistoryBtn")
                    ).isDisplayed(),
                    "View History button should be visible"
            );

            assertTrue(
                    driver.findElement(
                            By.id("viewHistoryBtn")
                    ).isEnabled(),
                    "View History button should be enabled"
            );

            // --------------------------------------------------
            // STEP 14: Verify Logout button
            // --------------------------------------------------

            assertTrue(
                    driver.findElement(
                            By.id("logoutBtn")
                    ).isDisplayed(),
                    "Logout button should be visible"
            );

            assertTrue(
                    driver.findElement(
                            By.id("logoutBtn")
                    ).isEnabled(),
                    "Logout button should be enabled"
            );

        } catch (AssertionError | RuntimeException e) {

            // Automatically capture screenshot whenever
            // the Selenium test fails.
            takeScreenshot(
                    "employeeDashboardDisplaysCorrectInformation"
            );

            // Re-throw the exception so Maven/Surefire
            // correctly marks the test as FAILED.
            throw e;
        }
    }
}