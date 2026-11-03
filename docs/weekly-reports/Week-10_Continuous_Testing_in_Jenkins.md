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

# Week 10: Continuous Testing in Jenkins

> **Official Syllabus Task**:
> Integrate the Performance Review Portal Selenium suite into Jenkins. Publish test reports and configure the pipeline so failed tests stop deployment. Correct at least one deliberately introduced defect and rerun the pipeline.

> **Mandatory Deliverable**:
> Jenkins test report, failed pipeline evidence, defect correction commit and successful rerun.

---

## 1. Selenium Quality Gate Integration in Jenkinsfile

```groovy
stage('Continuous Testing (Quality Gate)') {
    steps {
        script {
            echo "Executing automated headless Selenium regression test suite..."
            bat 'mvn test -Dtest=*Selenium*'
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
        failure {
            echo "QUALITY GATE FAILED! Selenium UI tests failed. Deployment immediately aborted."
        }
    }
}
```

---

## 2. Deliberately Introduced Defect Scenario
To demonstrate that the pipeline acts as an uncompromising quality gate:
- **Introduced Defect**: Modified `LoginTest.java` to assert an incorrect page title:
  ```java
  // Deliberate defect: expecting old string instead of current enterprise title
  assertEquals("Legacy Portal Sign In", driver.getTitle());
  ```
- **Pipeline Execution Result**:
  - The Selenium test failed with `AssertionFailedError: expected: <Legacy Portal Sign In> but was: <Sign In - Performance Review Portal>`.
  - Jenkins flagged the `Continuous Testing` stage in **RED**.
  - Subsequent deployment stage (`Deploy to Server`) was **ABORTED**.
  - Failure screenshot automatically recorded under `target/screenshots/testValidLogin_*.png`.

---

## 3. Defect Correction & Successful Rerun
- **Correction Commit**:
  ```java
  // Fixed assertion to match actual page title
  assertEquals("Sign In - Performance Review Portal", driver.getTitle());
  ```
- **Git Commit**: `git commit -am "fix(test): align login title assertion with enterprise redesign"`
- **Jenkins Rerun**: Build automatically triggered by Git push.
- **Rerun Result**: All 7 Selenium tests passed. Stage passed in **GREEN**, and deployment completed successfully!

---

## 4. Official Deliverable Sign-Off
- **Jenkins Test Report**: Surefire XML reports published in Jenkins dashboard.
- **Failed Pipeline Evidence**: Documented build abortion upon test failure.
- **Defect Correction Commit**: Committed and verified in Git history.
- **Successful Rerun**: Green pipeline run with successful downstream deployment.
