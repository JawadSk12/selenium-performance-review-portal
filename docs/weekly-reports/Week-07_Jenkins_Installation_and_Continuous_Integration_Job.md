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

# Week 07: Jenkins Installation and Continuous Integration Job

> **Official Syllabus Task**:
> Install and configure Jenkins. Connect the Performance Review Portal GitHub repository and create a Maven/Gradle/Ant build job triggered by commit or scheduled polling. Archive the build artefact.

> **Mandatory Deliverable**:
> Configured Jenkins job, successful build log, trigger evidence and archived artefact.

---

## 1. Jenkins Architecture & Installation
- **Jenkins Controller**: Running on port `8080` (or `8085` during port de-confliction).
- **Core Plugins Installed**:
  - Git Plugin (v5.2+)
  - Maven Integration Plugin (v3.23+)
  - Pipeline & Pipeline Stage View Plugins
  - JUnit Test Result Archiver Plugin
- **Global Tool Configuration**:
  - JDK: `Java 17 LTS` located at `JAVA_HOME`.
  - Maven: `Apache Maven 3.9.6`.

---

## 2. CI Job Configuration (`prp-ci-build`)
- **Job Name**: `prp-ci-build`
- **Source Code Management**: Git
  - Repository URL: `https://github.com/jawadsk/selenium-performance-review-portal.git`
  - Branch Specifier: `*/develop`
- **Build Trigger**: SCM Polling (`H/5 * * * *` — polls repository every 5 minutes for new commits).
- **Build Goals**: `clean package -DskipTests=false`
- **Post-Build Actions**: Archive artifacts `target/*.war`, `target/*.jar`.

---

## 3. Jenkins Build Execution Console Log

```
Started by an SCM change
Running as SYSTEM
Building in workspace C:\ProgramData\Jenkins\.jenkins\workspace\prp-ci-build
The recommended git tool is: NONE
using credential github-deploy-key
 > git.exe rev-parse --resolve-git-dir C:\ProgramData\Jenkins\.jenkins\workspace\prp-ci-build\.git # timeout=10
Fetching changes from the remote Git repository
 > git.exe config remote.origin.url https://github.com/jawadsk/selenium-performance-review-portal.git # timeout=10
Fetching upstream changes from origin
 > git.exe fetch --tags --force --progress -- origin +refs/heads/*:refs/remotes/origin/* # timeout=10
Checking out Revision 56310a6 (origin/develop)
[prp-ci-build] $ C:\Maven\bin\mvn.cmd clean package -DskipTests=false
[INFO] Scanning for projects...
[INFO] Building selenium-performance-review-portal 0.0.1-SNAPSHOT
[INFO] --- maven-compiler-plugin:3.13.0:compile ---
[INFO] Compiling 46 source files to C:\...\target\classes
[INFO] --- maven-surefire-plugin:3.2.5:test ---
[INFO] Running com.review.portal.EmployeeReviewWorkflowTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] Building war: C:\...\target\ROOT.war
[INFO] BUILD SUCCESS
[INFO] Total time: 38.412 s
Archiving artifacts
‘target/ROOT.war’ recorded
Finished: SUCCESS
```

---

## 4. Official Deliverable Sign-Off
- **Configured Jenkins Job**: `prp-ci-build` fully integrated with GitHub.
- **Successful Build Log**: Clean build and test execution documented.
- **Trigger Evidence**: Verified execution via automated SCM poll.
- **Archived Artefact**: `ROOT.war` recorded and downloadable from Jenkins build artifacts.
