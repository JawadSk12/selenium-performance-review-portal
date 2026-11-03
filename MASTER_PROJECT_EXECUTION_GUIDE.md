# 📘 MASTER PROJECT EXECUTION & DEVOPS HANDBOOK
## Selenium-Tested Enterprise Performance Review Portal
### Complete End-to-End Architectural, Operational, and Technical Guide

---

## 📑 TABLE OF CONTENTS
1. [Executive Summary & System Architecture](#1-executive-summary--system-architecture)
2. [Technology Stack Breakdown — What, Why, and How](#2-technology-stack-breakdown--what-why-and-how)
   - [Java 17 & Spring Boot 3.3.3](#21-java-17--spring-boot-333)
   - [Thymeleaf & Modern Enterprise UI](#22-thymeleaf--modern-enterprise-ui)
   - [PostgreSQL 16 Relational Database](#23-postgresql-16-relational-database)
   - [Apache Maven 3.x](#24-apache-maven-3x)
   - [Apache Tomcat 10.x](#25-apache-tomcat-10x)
   - [Selenium WebDriver 4 & Headless Chrome](#26-selenium-webdriver-4--headless-chrome)
   - [Docker & Dockerfile](#27-docker--dockerfile)
   - [Docker Compose Multi-Container Stack](#28-docker-compose-multi-container-stack)
   - [Jenkins CI/CD Pipeline](#29-jenkins-cicd-pipeline)
   - [Ansible Infrastructure Automation](#210-ansible-infrastructure-automation)
3. [Prerequisites & System Requirements](#3-prerequisites--system-requirements)
4. [Database Initialization & Seed Data Setup](#4-database-initialization--seed-data-setup)
5. [Complete Execution Walkthrough — All 5 Deployment Modes](#5-complete-execution-walkthrough--all-5-deployment-modes)
   - [Mode 1: Rapid Local Development (Embedded Spring Boot)](#mode-1-rapid-local-development-embedded-spring-boot)
   - [Mode 2: Automated Testing (Surefire + MockMvc + Selenium)](#mode-2-automated-testing-surefire--mockmvc--selenium)
   - [Mode 3: Production Tomcat 10 WAR Deployment](#mode-3-production-tomcat-10-war-deployment)
   - [Mode 4: Docker & Docker Compose Full-Stack Deployment](#mode-4-docker--docker-compose-full-stack-deployment)
   - [Mode 5: Jenkins Declarative CI/CD Pipeline](#mode-5-jenkins-declarative-cicd-pipeline)
   - [Mode 6: Ansible Provisioning & Health Validation](#mode-6-ansible-provisioning--health-validation)
6. [User Workflows, Routes & Access Credentials](#6-user-workflows-routes--access-credentials)
7. [Comprehensive Viva, Demo & Technical Interview Guide](#7-comprehensive-viva-demo--technical-interview-guide)
8. [Troubleshooting Matrix & Common Pitfalls](#8-troubleshooting-matrix--common-pitfalls)

---

## 1. EXECUTIVE SUMMARY & SYSTEM ARCHITECTURE

The **Selenium-Tested Performance Review Portal** is an enterprise-grade Human Resources web application engineered to demonstrate the complete software development and DevOps lifecycle. It bridges modern full-stack application development (Java 17, Spring Boot, Thymeleaf, PostgreSQL) with automated quality engineering (Selenium WebDriver, JUnit 5) and production infrastructure automation (Tomcat, Docker, Docker Compose, Jenkins CI/CD, Ansible).

### High-Level Architecture Flowchart

```
┌────────────────────────────────────────────────────────────────────────────────┐
│                             DEVELOPER WORKSTATION                              │
│         Git Push ──► GitHub Repository ──► Webhook / SCM Poll                  │
└──────────────────────────────────────┬─────────────────────────────────────────┘
                                       │
                                       ▼
┌────────────────────────────────────────────────────────────────────────────────┐
│                           JENKINS CI/CD SERVER                                 │
│  ┌───────────────────────┐   ┌───────────────────────┐   ┌───────────────────┐ │
│  │ Stage 1: Checkout     ├──►│ Stage 2: Maven Build  ├──►│ Stage 3: Prepare  │ │
│  │ (Git Source Pull)     │   │ (mvn clean compile)   │   │ Test App (Tomcat) │ │
│  └───────────────────────┘   └───────────────────────┘   └─────────┬─────────┘ │
│                                                                    │           │
│  ┌───────────────────────┐   ┌───────────────────────┐   ┌─────────▼─────────┐ │
│  │ Stage 6: Tomcat Deploy│◄──┤ Stage 5: Package WAR  │◄──┤ Stage 4: Testing  │ │
│  │ (Health Check on 8081)│   │ (mvn clean package)   │   │ (18 MockMvc +     │ │
│  └───────────────────────┘   └───────────────────────┘   │  5 Selenium Tests)│ │
│                                                          └───────────────────┘ │
└──────────────────────────────────────┬─────────────────────────────────────────┘
                                       │
            ┌──────────────────────────┴──────────────────────────┐
            ▼                                                     ▼
┌──────────────────────────────────────┐  ┌──────────────────────────────────────┐
│       DOCKER CONTAINERIZATION        │  │       ANSIBLE INFRASTRUCTURE         │
│  ┌─────────────────┐ ┌─────────────┐ │  │  ┌─────────────────────────────────┐ │
│  │ PostgreSQL 16   │ │ Spring Boot │ │  │  │ Playbook: site.yml              │ │
│  │ Container (5433)│◄┤ App (8082)  │ │  │  │ Playbook: week14-provision.yml  │ │
│  └─────────────────┘ └─────────────┘ │  │  │ Automated node provisioning,    │ │
│        Docker Compose Network        │  │  │ packages, users & health checks │ │
└──────────────────────────────────────┘  └──────────────────────────────────────┘
```

---

## 2. TECHNOLOGY STACK BREAKDOWN — WHAT, WHY, AND HOW

### 2.1 Java 17 & Spring Boot 3.3.3
* **What it is:** Enterprise Java runtime (LTS) paired with the modern Spring Boot framework.
* **Why it is used:** Provides a production-grade foundation with inversion of control (IoC), dependency injection (DI), robust transactional database handling, and rapid auto-configuration.
* **What it does in this project:**
  - **Controllers (`controller/`):** Route incoming HTTP requests for authentication (`/login`, `/manager/login`), employee dashboard (`/dashboard`), self-review submissions (`/review`), review history (`/employee/history`), and manager actions (`/manager/review/{id}`).
  - **Services (`service/`):** Encapsulate business logic, such as computing weighted competency scores, enforcing review submission states, and validating manager approval thresholds.
  - **Repositories (`repository/`):** Utilize Spring Data JPA to generate SQL queries dynamically via interface methods (e.g., `findByEmployeeOrderByReviewDateDesc`).
  - **Security Interceptors (`config/`):** Protect sensitive routes (such as `/manager/**` and `/review/**`) by verifying HTTP session attributes (`managerId`, `employeeId`).

### 2.2 Thymeleaf & Modern Enterprise UI
* **What it is:** Server-side Java XML/XHTML/HTML5 template engine.
* **Why it is used:** Enables seamless binding of backend Spring Model attributes to HTML templates without requiring a detached Single Page Application (SPA) framework, preserving fast server-side rendering and zero API drift.
* **What it does in this project:**
  - Dynamically renders employee metrics, status badges (`PENDING`, `APPROVED`, `REJECTED`), and manager queue counts.
  - Integrates Bootstrap 5.3, Bootstrap Icons, and Plus Jakarta Sans typography with glassmorphic cards, responsive sidebars, interactive slider scores, and accessible color-contrast ratios (WCAG 2.1 AA).

### 2.3 PostgreSQL 16 Relational Database
* **What it is:** Industry-standard enterprise open-source Object-Relational Database Management System (ORDBMS).
* **Why it is used:** Guarantees strict ACID compliance, relational integrity with foreign keys, and reliable transaction isolation.
* **What it does in this project:**
  - Persists tables: `manager`, `employee`, and `review`.
  - Enforces entity relationships: each `Employee` belongs to a `Manager` (`manager_id` foreign key), and each `Review` is tied to an `Employee` and reviewed by a `Manager`.
  - Managed via HikariCP connection pooling and Hibernate ORM.

### 2.4 Apache Maven 3.x
* **What it is:** Declarative build automation and project lifecycle management tool based on `pom.xml`.
* **Why it is used:** Manages external dependencies, compiles Java bytecode, executes automated test suites, and bundles the application into portable deployable artifacts.
* **What it does in this project:**
  - Coordinates compiler flags (`release 17`).
  - Runs Surefire test plugin (`maven-surefire-plugin:3.2.5`) to execute unit and browser tests.
  - Uses `maven-war-plugin` to package the build output into `target/performance-review-portal.war`.

### 2.5 Apache Tomcat 10.x
* **What it is:** Robust open-source Jakarta EE compliant Servlet web server and container.
* **Why it is used:** Industry-standard deployment target for enterprise WAR applications. Provides connection thread pools, session management, and classloader isolation.
* **What it does in this project:**
  - Acts as the primary production application server on port 8081.
  - The WAR is deployed as `ROOT.war` inside Tomcat's `webapps/` directory, exposing the application at the root context path (`/`).

### 2.6 Selenium WebDriver 4 & Headless Chrome
* **What it is:** Industry-standard browser automation framework capable of simulating real end-user interactions.
* **Why it is used:** Unit tests only verify isolated Java logic; Selenium proves the entire system works from the browser DOM level up, ensuring forms submit, CSS transitions render, and database records change upon user action.
* **What it does in this project:**
  - Executes 5 automated end-to-end browser test cases:
    1. `LoginTest`: Tests employee authentication and redirect.
    2. `LogoutTest`: Validates session invalidation and login page redirect.
    3. `DashboardTest`: Verifies metric card calculations, profile headers, and navigation buttons.
    4. `SubmitReviewTest`: Fills interactive rating sliders, enters achievements, submits the form, and asserts status transitions to `PENDING`.
    5. `ReviewHistoryTest`: Navigates to history tables and verifies chronological order of past appraisals.
  - Configured with headless Chrome (`--headless=new`) to run headlessly in continuous integration pipelines without requiring a physical display.

### 2.7 Docker & Dockerfile
* **What it is:** OS-level virtualization platform that packages software and its dependencies into lightweight, immutable containers.
* **Why it is used:** Eliminates "it works on my machine" syndrome by standardizing the exact Java runtime and environment across local developer laptops, staging nodes, and production servers.
* **What it does in this project:**
  - Built using official `eclipse-temurin:17-jre` base image.
  - Copies `target/performance-review-portal.war` into `/app/app.war` and launches it via executable jar entrypoint on internal port 8080.

### 2.8 Docker Compose Multi-Container Stack
* **What it is:** Multi-container orchestration tool defined through `docker-compose.yml`.
* **Why it is used:** Enables a developer or operator to launch the entire full-stack ecosystem (PostgreSQL database + Spring Boot web app) using a single command without installing software locally.
* **What it does in this project:**
  - Spins up container `performance-review-postgres` on host port `5433` with data volume persistence (`postgres_data`).
  - Spins up container `performance-review-app` on host port `8082`, injecting environment variables (`DB_HOST=postgres`, `DB_PORT=5432`, `DB_NAME=performance_review`).
  - Sets up an isolated bridge network allowing seamless container DNS communication.

### 2.9 Jenkins CI/CD Pipeline
* **What it is:** Open-source automation server enabling Continuous Integration and Continuous Delivery.
* **Why it is used:** Automates code checkout, compilation, regression testing, quality gating, and zero-downtime deployment, preventing broken code from ever reaching production.
* **What it does in this project:**
  - Configured via declarative `Jenkinsfile` across 6 structured stages:
    1. **Checkout:** Clones latest source code from Git repository.
    2. **Build:** Compiles code via `mvn clean compile`.
    3. **Prepare Test App:** Packages temporary WAR, deploys it to Tomcat `ROOT.war`, starts Tomcat, and polls health check until `200 OK`.
    4. **Continuous Testing:** Executes all 23 integration and Selenium browser tests. Publishes JUnit XML test reports. If any test fails, deployment is aborted immediately.
    5. **Package:** Re-packages final verified artifact `performance-review-portal.war`.
    6. **Deploy:** Copies final WAR to Tomcat, runs health check on `http://localhost:8081/login`, and certifies release.

### 2.10 Ansible Infrastructure Automation
* **What it is:** Agentless Infrastructure-as-Code (IaC) and configuration management tool based on YAML playbooks and SSH.
* **Why it is used:** Automates server provisioning, directory structure creation, security hardening, user permission assignments, and package installations deterministically.
* **What it does in this project:**
  - **`ansible/inventory.ini`:** Defines managed nodes (`localhost`).
  - **`ansible/site.yml`:** Provisions application user (`performance`), directories (`/opt/performance-review-portal`, `/var/log/...`), installs packages (`openjdk-17-jdk`, `maven`, `git`, `curl`), and performs port validation (5432 and 8082).
  - **`ansible/week14-provision.yml`:** Automates reliability validation, configuration deployment, and release tracking.

---

## 3. PREREQUISITES & SYSTEM REQUIREMENTS

Before running the application across any mode, ensure your host machine has the following tools installed and accessible in the system PATH:

| Tool | Recommended Version | Verification Command |
| :--- | :--- | :--- |
| **Java JDK** | OpenJDK / Eclipse Temurin 17 | `java -version` |
| **Apache Maven** | 3.9+ | `mvn -version` |
| **PostgreSQL** | 16.x | `psql -U postgres -V` |
| **Google Chrome** | Latest Stable (120+) | `chrome --version` |
| **Apache Tomcat** | 10.1+ (extracted at `C:\Tomcat`) | `C:\Tomcat\bin\catalina.bat version` |
| **Docker Desktop** | 24+ with Compose v2 | `docker compose version` |
| **Jenkins** | 2.400+ | Running on `http://localhost:8080` |
| **Git** | 2.40+ | `git --version` |

---

## 4. DATABASE INITIALIZATION & SEED DATA SETUP

The application connects to a PostgreSQL database named `performance_review`.

### Step 1: Create Database in PostgreSQL
Open PowerShell or Command Prompt:

```powershell
# Connect to PostgreSQL CLI
psql -U postgres -h localhost

# Inside PostgreSQL prompt, create the database:
CREATE DATABASE performance_review;
\q
```

### Step 2: Seed Enterprise Demo Data
The project includes a ready-to-use seed script [`seed-data.sql`](file:///c:/PROJECT/selenium-performance-review-portal-main/selenium-performance-review-portal-main/seed-data.sql) containing:
- 1 Manager account (`Ahmed Khan`, `manager@gmail.com`)
- 6 Diverse Employees (IT, Cloud, Frontend, QA, Backend, DevOps)
- Pre-populated pending and approved performance appraisals

Run the seed script from the project root:

```powershell
cd "C:\PROJECT\selenium-performance-review-portal-main\selenium-performance-review-portal-main"
psql -U postgres -d performance_review -f seed-data.sql
```

---

## 5. COMPLETE EXECUTION WALKTHROUGH — ALL 5 DEPLOYMENT MODES

### Mode 1: Rapid Local Development (Embedded Spring Boot)
*Best for daily frontend/backend coding and instant hot reload.*

1. Ensure PostgreSQL service is running on `localhost:5432`.
2. Open PowerShell in the project directory:
   ```powershell
   cd "C:\PROJECT\selenium-performance-review-portal-main\selenium-performance-review-portal-main"
   mvn spring-boot:run
   ```
3. Open your browser:
   - **Employee Portal:** [http://localhost:8081/login](http://localhost:8081/login)
   - **Executive Manager Portal:** [http://localhost:8081/manager/login](http://localhost:8081/manager/login)

---

### Mode 2: Automated Testing (Surefire + MockMvc + Selenium)
*Runs all 23 tests (18 Spring MockMvc integration tests + 5 Selenium browser tests).*

```powershell
cd "C:\PROJECT\selenium-performance-review-portal-main\selenium-performance-review-portal-main"

# Execute entire test suite
mvn test
```

> **How it works autonomously:**  
> You do **not** need to manually start Tomcat. [`BaseTest.java`](file:///c:/PROJECT/selenium-performance-review-portal-main/selenium-performance-review-portal-main/src/test/java/com/review/portal/selenium/BaseTest.java) checks if port 8081 is listening. If idle, it verifies PostgreSQL, boots an embedded Spring Boot instance on port 8081, runs all Selenium browser tests headlessly, and shuts down cleanly via a registered JVM hook.

---


### Mode 3: Production Tomcat 10 WAR Deployment
*Simulates real enterprise deployment to an external application server.*

1. **Build the WAR package:**
   ```powershell
   cd "C:\PROJECT\selenium-performance-review-portal-main\selenium-performance-review-portal-main"
   mvn clean package -DskipTests
   ```
   *Output generated at:* `target/performance-review-portal.war`

2. **Deploy to Tomcat:**
   ```powershell
   # Stop Tomcat if running
   & "C:\Tomcat\bin\shutdown.bat"

   # Remove old ROOT deployment
   Remove-Item -Recurse -Force "C:\Tomcat\webapps\ROOT" -ErrorAction SilentlyContinue
   Remove-Item -Force "C:\Tomcat\webapps\ROOT.war" -ErrorAction SilentlyContinue

   # Copy WAR as ROOT.war (ensures context path is /)
   Copy-Item "target\performance-review-portal.war" "C:\Tomcat\webapps\ROOT.war"

   # Start Tomcat
   & "C:\Tomcat\bin\startup.bat"
   ```

3. **Verify:**
   Open [http://localhost:8081/login](http://localhost:8081/login) in your browser.

---

### Mode 4: Docker & Docker Compose Full-Stack Deployment
*Runs the application and database inside completely isolated containers.*

1. **Build the deployable WAR first:**
   ```powershell
   mvn clean package -DskipTests
   ```

2. **Build the Docker application image:**
   ```powershell
   docker build -t performance-review-portal:week11 .
   ```

3. **Launch the multi-container stack:**
   ```powershell
   docker compose up -d
   ```

4. **Verify container status:**
   ```powershell
   docker compose ps
   ```
   You will see:
   - `performance-review-postgres`: Running on host port `5433` -> container port `5432`.
   - `performance-review-app`: Running on host port `8082` -> container port `8080`.

5. **Access Containerized App:**
   Open [http://localhost:8082/login](http://localhost:8082/login).

6. **Teardown Stack when finished:**
   ```powershell
   docker compose down
   ```

---

### Mode 5: Jenkins Declarative CI/CD Pipeline
*Automates the complete delivery lifecycle from Git push to production deployment.*

1. **Open Jenkins Dashboard:** `http://localhost:8080`.
2. **Create New Pipeline Job:**
   - Name: `Performance-Review-Portal-CI-CD`
   - Type: **Pipeline**
3. **Configure Pipeline Definition:**
   - Under **Pipeline**, select **Pipeline script from SCM**.
   - SCM: **Git**
   - Repository URL: Enter your local or GitHub repository URL.
   - Script Path: `Jenkinsfile`
4. **Trigger Build:**
   - Click **Build with Parameters** -> Choose `DEPLOY_ENV: staging` -> Click **Build**.
5. **Observed Pipeline Stages in Jenkins UI:**
   ```
   [Checkout] ──► [Build] ──► [Prepare Test App] ──► [Continuous Testing] ──► [Package] ──► [Deploy]
      SUCCESS        SUCCESS           SUCCESS                  SUCCESS              SUCCESS     SUCCESS
   ```
   - **Quality Gate:** If any Selenium or JUnit test fails during Stage 4, Stages 5 and 6 are blocked, preventing faulty releases.
   - **Test Results:** View the interactive test trend graph via Jenkins JUnit report publisher (`target/surefire-reports/*.xml`).

---

### Mode 6: Ansible Provisioning & Health Validation
*Automated configuration management for Linux / WSL nodes.*

1. Open WSL (Ubuntu) or an SSH-connected target node:
   ```bash
   cd /mnt/c/PROJECT/selenium-performance-review-portal-main/selenium-performance-review-portal-main/ansible
   ```

2. **Run Ansible Playbook:**
   ```bash
   ansible-playbook -i inventory.ini site.yml --ask-become-pass
   ```

3. **What Ansible executes automatically:**
   - Updates `apt` cache and installs `openjdk-17-jdk`, `maven`, `git`, `curl`, and `net-tools`.
   - Creates system group `performance` and user `performance`.
   - Configures deployment directories: `/opt/performance-review-portal`, `/var/log/...`, `/etc/...`.
   - Writes `application-config.properties`.
   - Validates that port 5432 (PostgreSQL) and port 8082 (App) are responsive.

---

## 6. USER WORKFLOWS, ROUTES & ACCESS CREDENTIALS

### 🔐 Demo Credentials
All seeded demo accounts share the password: **`1234`**

| Role | Name | Email | Password | Department / Specialization |
| :--- | :--- | :--- | :--- | :--- |
| **Manager** | Ahmed Khan | `manager@gmail.com` | `1234` | IT Management (All Teams) |
| **Employee** | Jawad Shaikh | `jawad@gmail.com` | `1234` | Software Engineering |
| **Employee** | Aisha Patel | `aisha.patel@company.com` | `1234` | Cloud Solutions Engineer |
| **Employee** | Carlos Rodriguez | `carlos.r@company.com` | `1234` | Senior UI/UX Engineer |
| **Employee** | Emily Watson | `emily.watson@company.com` | `1234` | QA Automation Lead |
| **Employee** | Michael Chang | `michael.c@company.com` | `1234` | Software Engineer II |
| **Employee** | Priya Sharma | `priya.s@company.com` | `1234` | Site Reliability Engineer |

---

### 🌐 Key Application URLs

| Route | Role | Description |
| :--- | :--- | :--- |
| `/login` | Public | Employee portal login screen |
| `/dashboard` | Employee | Main dashboard displaying metrics, status badge, action buttons |
| `/review` | Employee | Core competency appraisal form with 4 live interactive sliders |
| `/employee/history` | Employee | Complete chronological appraisal history with manager feedback |
| `/manager/login` | Public | Dark-glassmorphic executive workspace login |
| `/manager/dashboard` | Manager | Appraisal queue, quick statistics, pending reviews list |
| `/manager/review/{id}` | Manager | Detailed review evaluation screen: approve or reject with manager comments |
| `/logout` | Any | Clears session and redirects to login |

---

## 7. COMPREHENSIVE VIVA, DEMO & TECHNICAL INTERVIEW GUIDE

When presenting this project to interviewers, examiners, or project stakeholders, use these concise, professional explanations:

### Q1: "Why did you use Selenium instead of just relying on MockMvc?"
> *"MockMvc tests Spring controllers inside a mock web server environment without a real browser engine. While fast, it cannot test JavaScript DOM interactions, CSS layout responsiveness, multi-step browser redirects, or real browser cookie behavior. Selenium WebDriver runs headlessly inside real Chrome, proving the system works from end to end exactly as an actual employee or manager experiences it."*

### Q2: "What role does Tomcat play when Spring Boot already has an embedded server?"
> *"Spring Boot provides an embedded Tomcat for rapid development convenience, but in enterprise IT environments, organizations often standardize on standalone application servers. We configured Maven's `<packaging>war</packaging>` and extended `SpringBootServletInitializer`, allowing the exact same codebase to run either standalone (`mvn spring-boot:run`) or as a traditional `ROOT.war` inside dedicated Tomcat servers."*

### Q3: "Explain how your Jenkins pipeline prevents broken deployments."
> *"In our `Jenkinsfile`, Stage 4 is 'Continuous Testing'. It runs all unit, integration, and Selenium WebDriver tests before the packaging and deployment stages. If a developer accidentally breaks a UI element or business rule, Surefire reports a build failure, Jenkins automatically halts the pipeline, and the broken code is never copied into Tomcat's `webapps/` folder."*

### Q4: "Why use both Docker and Ansible? Don't they do the same thing?"
> *"No, they complement each other. Docker provides container virtualization—it packages the application code, dependencies, and JRE into an immutable container image. Ansible provides configuration management and Infrastructure-as-Code (IaC)—it automates host server provisioning, security groups, system users, network checks, and OS package updates across physical or virtual target machines."*

### Q5: "How does Docker Compose handle data persistence for PostgreSQL?"
> *"In `docker-compose.yml`, we declared a named volume called `postgres_data` mapped to `/var/lib/postgresql/data`. Even when containers are stopped, destroyed, or upgraded via `docker compose down`, the underlying database tables and records remain safely stored on the host filesystem and automatically remount upon restart."*

---

## 8. TROUBLESHOOTING MATRIX & COMMON PITFALLS

| Symptom | Root Cause | Solution |
| :--- | :--- | :--- |
| `ERR_CONNECTION_REFUSED` on port 8081 during Selenium tests | Local test server was not running | Run `mvn test`. The autonomous `@BeforeAll` in `BaseTest.java` will automatically boot an in-process instance on port 8081. |
| `PostgreSQL is not reachable on localhost:5432` | PostgreSQL Windows service is stopped | Start PostgreSQL via Windows Services (`services.msc`) or run `net start postgresql-x64-16`. |
| Port 8081 already in use | Stale Tomcat or Java process lingering | In PowerShell, run: `Get-Process -Id (Get-NetTCPConnection -LocalPort 8081).OwningProcess \| Stop-Process -Force`. |
| Jenkins build fails on `Preparation Test Application` | Tomcat path in `Jenkinsfile` differs from actual install | Ensure `TOMCAT_HOME = 'C:\\Tomcat'` matches your installation path. |
| Docker container cannot connect to database | `DB_HOST` set to `localhost` inside container | Ensure `DB_HOST=postgres` in `docker-compose.yml` so Docker's internal DNS resolves the container name. |

---

*Authored for the Selenium-Tested Enterprise Performance Review Portal. Fully verified across all 23 automated tests and DevOps deployment stages.*

