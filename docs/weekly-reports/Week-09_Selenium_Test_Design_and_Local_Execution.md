---
# Academic Portfolio & Technical Documentation
- **Student Name**: JAWAD SHAIKH
- **Roll Number**: 23102B0009
- **Class / Division / Batch**: BE - VII | Division B | Batch-1
- **Academic Year**: 2024–2025 (Semester VII)
- **Course**: DevOps Laboratory & Continuous Quality Engineering
- **Department**: Department of Computer Engineering
- **Project Title**: Selenium-Tested Performance Review Portal
- **Technology Stack**: Java 17 LTS, Spring Boot 3.3.3, PostgreSQL 16, Maven, Tomcat 10, Selenium WebDriver 4.24, Jenkins CI/CD, Docker, Ansible
---

# Week 09: Selenium Test Design and Local Execution

> **Official Syllabus Task**:
> Identify three to five critical user journeys in the Performance Review Portal. Write Selenium WebDriver test cases with assertions, test data and screenshots for failures. Execute the suite locally through Maven.

> **Mandatory Deliverable**:
> Test plan, Selenium scripts, local test report and failure screenshot mechanism.

---

## 1. Selenium Test Automation Framework Architecture
- **Selenium WebDriver Version**: 4.24.0
- **Test Engine**: JUnit 5 Jupiter (`@Test`, `@BeforeEach`, `@AfterEach`, `@ExtendWith(ScreenshotOnFailureExtension.class)`)
- **Browser Execution**: Google Chrome in Headless Mode (`--headless=new`, `--disable-gpu`, `--no-sandbox`, `--remote-allow-origins=*`)
- **Design Pattern**: Page Object Model (POM) separating UI locators from test logic.

---

## 2. 5 Critical User Journeys Implemented

| Test Class | Critical User Journey | Key Assertions |
| :--- | :--- | :--- |
| **`LoginTest.java`** | Employee Authentication | Validates successful redirection to `/dashboard`, verifies session flash message, checks invalid password error banner. |
| **`DashboardTest.java`** | Metrics & Quick Navigation | Asserts visibility of user name (`Jawad Shaikh`), KPI statistic cards, and action button links. |
| **`SubmitReviewTest.java`** | Self-Appraisal Submission | Fills score inputs (9, 9, 8, 10), achievements text, clicks submit, asserts redirection to `/review/history` with success alert. |
| **`ReviewHistoryTest.java`** | Historical Ledger Inspection | Verifies table rendering, presence of submitted record, and correct `PENDING` badge class. |
| **`LogoutTest.java`** | Session Invalidation | Clicks sign-out button, verifies cookie clearing, asserts redirect to `/login?logout=true`. |

---

## 3. Automated Failure Screenshot Capture Mechanism

```java
public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Boolean testFailed = context.getExecutionException().isPresent();
        if (testFailed) {
            WebDriver driver = BaseTest.getDriver();
            if (driver instanceof TakesScreenshot takesScreenshot) {
                File source = takesScreenshot.getScreenshotAs(OutputType.FILE);
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String testName = context.getRequiredTestMethod().getName();
                File destination = new File("target/screenshots/" + testName + "_" + timestamp + ".png");
                destination.getParentFile().mkdirs();
                Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.err.println("Failure screenshot saved to: " + destination.getAbsolutePath());
            }
        }
    }
}
```

---

## 4. Local Execution Test Report Output

```bash
mvn test -Dtest=*Selenium*
```

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.review.portal.selenium.LoginTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.812 s
[INFO] Running com.review.portal.selenium.DashboardTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.124 s
[INFO] Running com.review.portal.selenium.SubmitReviewTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.418 s
[INFO] Running com.review.portal.selenium.ReviewHistoryTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.902 s
[INFO] Running com.review.portal.selenium.LogoutTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.210 s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

---

## 5. Official Deliverable Sign-Off
- **Test Plan**: 5 critical end-to-end user journeys defined.
- **Selenium Scripts**: Cleanly structured and executable in headless mode.
- **Local Test Report**: 100% pass rate recorded across all 7 test cases.
- **Failure Screenshot Mechanism**: `ScreenshotOnFailureExtension` configured and active.
