package com.review.portal.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    @Test
    public void successfulEmployeeLogin() {

        try {

            // Step 1: Open login page
            openLoginPage();

            // Verify login page
            assertEquals(
                    "Login - Employee Performance Review Portal",
                    driver.getTitle()
            );

            // Step 2: Enter email
            driver.findElement(By.id("email"))
                    .clear();

            driver.findElement(By.id("email"))
                    .sendKeys("jawad@gmail.com");

            // Step 3: Enter password
            driver.findElement(By.id("password"))
                    .clear();

            driver.findElement(By.id("password"))
                    .sendKeys("1234");

            // Step 4: Click Sign In
            driver.findElement(By.id("loginBtn"))
                    .click();

            // Step 5: Wait until dashboard loads
            wait.until(driver ->
                    driver.getCurrentUrl().contains("/dashboard")
            );

            // Assertion 1: URL
            assertTrue(
                    driver.getCurrentUrl().contains("/dashboard"),
                    "User should be redirected to dashboard after successful login"
            );

            // Assertion 2: Page title
            assertEquals(
                    "Employee Dashboard - Performance Review Portal",
                    driver.getTitle()
            );

            // Assertion 3: Dashboard heading
            assertTrue(
                    driver.getPageSource()
                            .contains("Employee Dashboard"),
                    "Employee Dashboard heading should be visible"
            );

            // Assertion 4: Employee name
            assertTrue(
                    driver.findElement(
                            By.id("employeeNameDisplay")
                    ).isDisplayed(),
                    "Employee name should be displayed on dashboard"
            );

            assertEquals(
                    "Jawad Shaikh",
                    driver.findElement(
                            By.id("employeeNameDisplay")
                    ).getText()
            );

        } catch (AssertionError | RuntimeException e) {

            takeScreenshot("successfulEmployeeLogin");

            throw e;
        }
    }
}