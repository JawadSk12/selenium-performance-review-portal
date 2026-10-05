package com.review.portal.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogoutTest extends BaseTest {

    @Test
    public void employeeCanLogoutSuccessfully() {

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
            // STEP 3: Wait for Employee Dashboard
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
            // STEP 4: Verify Employee Dashboard
            // =========================================================

            WebElement employeeName =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("employeeNameDisplay")
                            )
                    );

            assertTrue(
                    employeeName.isDisplayed(),
                    "Employee name should be displayed"
            );

            assertEquals(
                    "Jawad Shaikh",
                    employeeName.getText().trim(),
                    "Incorrect employee name"
            );


            // =========================================================
            // STEP 5: Locate Logout Button
            // =========================================================

            WebElement logoutButton =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("logoutBtn")
                            )
                    );

            assertTrue(
                    logoutButton.isDisplayed(),
                    "Logout button should be visible"
            );

            assertTrue(
                    logoutButton.isEnabled(),
                    "Logout button should be enabled"
            );


            // =========================================================
            // STEP 6: Click Logout
            // =========================================================

            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            js.executeScript(
                    "arguments[0].scrollIntoView({" +
                            "behavior:'instant'," +
                            "block:'center'," +
                            "inline:'center'" +
                            "});",
                    logoutButton
            );

            js.executeScript(
                    "arguments[0].click();",
                    logoutButton
            );


            // =========================================================
            // STEP 7: Wait for Login Page
            // =========================================================

            wait.withTimeout(
                    Duration.ofSeconds(15)
            ).until(
                    driver ->
                            driver.getCurrentUrl()
                                    .contains("/login")
            );


            // =========================================================
            // STEP 8: Verify Redirect to Login Page
            // =========================================================

            assertTrue(
                    driver.getCurrentUrl().contains("/login"),
                    "Employee should be redirected to login page after logout"
            );

            assertEquals(
                    "Login - Employee Performance Review Portal",
                    driver.getTitle(),
                    "Login page title should be correct after logout"
            );


            // =========================================================
            // STEP 9: Verify Login Form Is Visible
            // =========================================================

            WebElement loginForm =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("loginForm")
                            )
                    );

            assertTrue(
                    loginForm.isDisplayed(),
                    "Login form should be visible after logout"
            );


            // =========================================================
            // STEP 10: Verify Login Fields Are Available
            // =========================================================

            WebElement emailField =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("email")
                            )
                    );

            WebElement passwordField =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("password")
                            )
                    );

            WebElement loginButton =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("loginBtn")
                            )
                    );

            assertTrue(
                    emailField.isDisplayed(),
                    "Email field should be visible after logout"
            );

            assertTrue(
                    passwordField.isDisplayed(),
                    "Password field should be visible after logout"
            );

            assertTrue(
                    loginButton.isDisplayed(),
                    "Login button should be visible after logout"
            );


            // =========================================================
            // STEP 11: Print Successful Test Information
            // =========================================================

            System.out.println();

            System.out.println(
                    "================================================="
            );

            System.out.println(
                    "LOGOUT TEST PASSED"
            );

            System.out.println(
                    "Employee: Jawad Shaikh"
            );

            System.out.println(
                    "Logout button verified"
            );

            System.out.println(
                    "Logout executed successfully"
            );

            System.out.println(
                    "Redirected to login page"
            );

            System.out.println(
                    "Login page title verified"
            );

            System.out.println(
                    "Login form verified"
            );

            System.out.println(
                    "Login fields verified"
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
                    "employeeCanLogoutSuccessfully"
            );

            throw e;
        }
    }
}