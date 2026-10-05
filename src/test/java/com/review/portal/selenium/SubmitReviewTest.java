package com.review.portal.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubmitReviewTest extends BaseTest {

    @Test
    public void employeeCanSubmitSelfReview() {

        try {

            // ==========================================================
            // STEP 1: Open Login Page
            // ==========================================================

            openLoginPage();

            assertTrue(
                    driver.findElement(By.id("loginForm")).isDisplayed(),
                    "Login form should be displayed"
            );

            // ==========================================================
            // STEP 2: Login as Employee
            // ==========================================================

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

            wait.until(
                    driver ->
                            driver.getCurrentUrl()
                                    .contains("/dashboard")
            );

            assertTrue(
                    driver.getCurrentUrl().contains("/dashboard"),
                    "Employee should be redirected to dashboard after login"
            );

            // ==========================================================
            // STEP 3: Open Review Page
            // ==========================================================

            driver.get(BASE_URL + "/review");

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("selfReviewForm")
                    )
            );

            assertTrue(
                    driver.getCurrentUrl().contains("/review"),
                    "Employee should be on review page"
            );

            assertTrue(
                    driver.findElement(
                            By.id("selfReviewForm")
                    ).isDisplayed(),
                    "Self review form should be displayed"
            );

            // ==========================================================
            // STEP 4: Set Technical Rating = 8
            // ==========================================================

            setSliderValue(
                    By.id("technical"),
                    8
            );

            // ==========================================================
            // STEP 5: Set Communication Rating = 8
            // ==========================================================

            setSliderValue(
                    By.id("communication"),
                    8
            );

            // ==========================================================
            // STEP 6: Set Teamwork Rating = 9
            // ==========================================================

            setSliderValue(
                    By.id("teamwork"),
                    9
            );

            // ==========================================================
            // STEP 7: Set Problem Solving Rating = 9
            // ==========================================================

            setSliderValue(
                    By.id("problemSolving"),
                    9
            );

            // ==========================================================
            // STEP 8: Enter Achievement
            // ==========================================================

            String achievementText =
                    "Successfully completed assigned development tasks " +
                    "and improved application functionality.";

            setTextareaValue(
                    By.id("achievement"),
                    achievementText
            );

            // ==========================================================
            // STEP 9: Enter Future Goal
            // ==========================================================

            String futureGoalText =
                    "Improve technical skills, contribute to larger projects " +
                    "and take more responsibility.";

            setTextareaValue(
                    By.id("futureGoal"),
                    futureGoalText
            );

            // ==========================================================
            // STEP 10: Verify Achievement
            // ==========================================================

            assertEquals(
                    achievementText,
                    driver.findElement(
                            By.id("achievement")
                    ).getDomProperty("value"),
                    "Achievement text was not entered correctly"
            );

            // ==========================================================
            // STEP 11: Verify Future Goal
            // ==========================================================

            assertEquals(
                    futureGoalText,
                    driver.findElement(
                            By.id("futureGoal")
                    ).getDomProperty("value"),
                    "Future goal text was not entered correctly"
            );

            // ==========================================================
            // STEP 12: Verify Technical Rating
            // ==========================================================

            assertEquals(
                    "8",
                    driver.findElement(
                            By.id("technical")
                    ).getDomProperty("value"),
                    "Technical rating should be 8"
            );

            // ==========================================================
            // STEP 13: Verify Communication Rating
            // ==========================================================

            assertEquals(
                    "8",
                    driver.findElement(
                            By.id("communication")
                    ).getDomProperty("value"),
                    "Communication rating should be 8"
            );

            // ==========================================================
            // STEP 14: Verify Teamwork Rating
            // ==========================================================

            assertEquals(
                    "9",
                    driver.findElement(
                            By.id("teamwork")
                    ).getDomProperty("value"),
                    "Teamwork rating should be 9"
            );

            // ==========================================================
            // STEP 15: Verify Problem Solving Rating
            // ==========================================================

            assertEquals(
                    "9",
                    driver.findElement(
                            By.id("problemSolving")
                    ).getDomProperty("value"),
                    "Problem solving rating should be 9"
            );

            // ==========================================================
            // STEP 16: Find Submit Button
            // ==========================================================

            WebElement submitButton =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("submitReviewBtn")
                            )
                    );

            assertTrue(
                    submitButton.isDisplayed(),
                    "Submit Review button should be visible"
            );

            assertTrue(
                    submitButton.isEnabled(),
                    "Submit Review button should be enabled"
            );

            // ==========================================================
            // STEP 17: Scroll Submit Button Into View
            // ==========================================================

            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            js.executeScript(
                    "arguments[0].scrollIntoView({"
                            + "behavior:'instant',"
                            + "block:'center',"
                            + "inline:'center'"
                            + "});",
                    submitButton
            );

            // ==========================================================
            // STEP 18: Submit Review
            // ==========================================================

            /*
             * Actual application HTML:
             *
             * <form id="selfReviewForm">
             *
             * <button
             *     type="submit"
             *     id="submitReviewBtn">
             *
             * The JavaScript click triggers the real browser
             * click on the submit button.
             */

            js.executeScript(
                    "arguments[0].click();",
                    submitButton
            );

            // ==========================================================
            // STEP 19: Wait for Dashboard
            // ==========================================================

            wait.withTimeout(
                    Duration.ofSeconds(20)
            ).until(
                    driver ->
                            driver.getCurrentUrl()
                                    .contains("/dashboard")
            );

            // ==========================================================
            // STEP 20: Verify Dashboard Redirect
            // ==========================================================

            assertTrue(
                    driver.getCurrentUrl().contains("/dashboard"),
                    "Employee should be redirected to dashboard after submitting review"
            );

            // ==========================================================
            // STEP 21: Verify Employee Name
            // ==========================================================

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("employeeNameDisplay")
                    )
            );

            assertEquals(
                    "Jawad Shaikh",
                    driver.findElement(
                            By.id("employeeNameDisplay")
                    ).getText(),
                    "Employee name should be displayed after submission"
            );

            // ==========================================================
            // STEP 22: Verify Success Message
            // ==========================================================

            String pageText =
                    driver.findElement(
                            By.tagName("body")
                    ).getText();

            assertTrue(
                    pageText.contains("Success!")
                            || pageText.contains(
                                    "Your performance review has been submitted successfully!"
                            ),
                    "Successful review submission message should be displayed"
            );

            // ==========================================================
            // STEP 23: Verify PENDING Status
            // ==========================================================

            assertTrue(
                    pageText.contains("Status: PENDING"),
                    "Review status should be PENDING after submission"
            );

            // ==========================================================
            // TEST PASSED
            // ==========================================================

            System.out.println();
            System.out.println(
                    "================================================="
            );
            System.out.println(
                    "SELF REVIEW SUBMISSION TEST PASSED"
            );
            System.out.println(
                    "Employee: Jawad Shaikh"
            );
            System.out.println(
                    "Technical: 8"
            );
            System.out.println(
                    "Communication: 8"
            );
            System.out.println(
                    "Teamwork: 9"
            );
            System.out.println(
                    "Problem Solving: 9"
            );
            System.out.println(
                    "Achievement: Entered successfully"
            );
            System.out.println(
                    "Future Goal: Entered successfully"
            );
            System.out.println(
                    "Review submitted successfully"
            );
            System.out.println(
                    "Status: PENDING"
            );
            System.out.println(
                    "================================================="
            );
            System.out.println();

        } catch (AssertionError | RuntimeException e) {

            // ==========================================================
            // FAILURE SCREENSHOT
            // ==========================================================

            takeScreenshot(
                    "employeeCanSubmitSelfReview"
            );

            throw e;
        }
    }

    // ==================================================================
    // HELPER METHOD: SET RANGE SLIDER VALUE
    // ==================================================================

    private void setSliderValue(
            By locator,
            int targetValue
    ) {

        WebElement slider =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                locator
                        )
                );

        assertEquals(
                "range",
                slider.getAttribute("type"),
                "Element should be a range slider: " + locator
        );

        assertEquals(
                "1",
                slider.getAttribute("min"),
                "Slider minimum should be 1: " + locator
        );

        assertEquals(
                "10",
                slider.getAttribute("max"),
                "Slider maximum should be 10: " + locator
        );

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                """
                const slider = arguments[0];
                const value = arguments[1];

                const nativeSetter =
                    Object.getOwnPropertyDescriptor(
                        HTMLInputElement.prototype,
                        'value'
                    ).set;

                nativeSetter.call(
                    slider,
                    String(value)
                );

                slider.dispatchEvent(
                    new Event('input', {
                        bubbles: true
                    })
                );

                slider.dispatchEvent(
                    new Event('change', {
                        bubbles: true
                    })
                );
                """,
                slider,
                targetValue
        );

        assertEquals(
                String.valueOf(targetValue),
                slider.getDomProperty("value"),
                "Slider value was not set correctly for "
                        + locator
        );
    }

    // ==================================================================
    // HELPER METHOD: SET TEXTAREA VALUE
    // ==================================================================

    private void setTextareaValue(
            By locator,
            String text
    ) {

        WebElement textarea =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                locator
                        )
                );

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                """
                const textarea = arguments[0];
                const value = arguments[1];

                const nativeSetter =
                    Object.getOwnPropertyDescriptor(
                        HTMLTextAreaElement.prototype,
                        'value'
                    ).set;

                nativeSetter.call(
                    textarea,
                    value
                );

                textarea.dispatchEvent(
                    new Event('input', {
                        bubbles: true
                    })
                );

                textarea.dispatchEvent(
                    new Event('change', {
                        bubbles: true
                    })
                );
                """,
                textarea,
                text
        );

        assertEquals(
                text,
                textarea.getDomProperty("value"),
                "Textarea value was not entered correctly for "
                        + locator
        );
    }
}