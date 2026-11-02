# 🏢 Performance Review Portal

> A full-stack **Spring Boot** enterprise application for managing employee performance reviews — built, tested, packaged, containerised, and deployed through a complete **DevOps pipeline** using Jenkins CI/CD, Docker, Docker Compose, and Ansible automation.
<!-- Verified DevOps CI/CD Pipeline - Release v1.0.1 -->

---

## 📋 Table of Contents

1. [Project Overview](#-project-overview)
2. [Tech Stack](#-tech-stack)
3. [Architecture](#-architecture)
4. [Repository Structure](#-repository-structure)
5. [Prerequisites](#-prerequisites)
6. [Local Development Setup](#-local-development-setup)
7. [Environment Variables](#-environment-variables)
8. [Building the Application](#-building-the-application)
9. [Running the Application](#-running-the-application)
10. [Docker — Containerised Deployment](#-docker--containerised-deployment)
11. [Docker Compose — Full Stack](#-docker-compose--full-stack)
12. [Selenium Automated Testing](#-selenium-automated-testing)
13. [Jenkins CI/CD Pipeline](#-jenkins-cicd-pipeline)
14. [Ansible — Infrastructure Provisioning](#-ansible--infrastructure-provisioning)
15. [Week-by-Week DevOps Milestones](#-week-by-week-devops-milestones)
16. [Application Endpoints & URLs](#-application-endpoints--urls)
17. [Troubleshooting](#-troubleshooting)
18. [Contributing](#-contributing)

---

## 🔍 Project Overview

The **Performance Review Portal** is a Java web application that allows:

- **Employees** to submit self-performance reviews and view their review history.
- **Managers** to log in, view their team's submissions, evaluate/approve reviews, and manage employee records.

The project is built as a **WAR-packaged Spring Boot application** deployed to Apache Tomcat. It demonstrates a real-world DevOps workflow across multiple delivery weeks — from source control and CI with Jenkins, to containerisation with Docker, infrastructure provisioning with Ansible, and end-to-end automated browser testing with Selenium WebDriver.

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| **Backend** | Java 17, Spring Boot 3.3.3 |
| **View Layer** | Thymeleaf (server-side templates) |
| **ORM / Persistence** | Spring Data JPA + Hibernate |
| **Database** | PostgreSQL 16 |
| **Security** | Spring Security Crypto (BCrypt) |
| **Build Tool** | Apache Maven 3.x |
| **Packaging** | WAR (deployable to Tomcat / standalone) |
| **Application Server** | Apache Tomcat 10.x |
| **Containerisation** | Docker, Docker Compose |
| **CI/CD** | Jenkins (Declarative Pipeline) |
| **Infrastructure Automation** | Ansible |
| **Testing** | JUnit 5, Selenium WebDriver 4.50, ChromeDriver |
| **Validation** | Hibernate Validator (Bean Validation) |
| **Code Generation** | Lombok |

---

## 🏛 Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Developer Workstation                     │
│  Git Push ──► GitHub ──► Jenkins Webhook ──► Pipeline Run  │
└─────────────────────────────────────────────────────────────┘
                              │
          ┌───────────────────▼──────────────────┐
          │          Jenkins CI/CD Server         │
          │  Stage 1: Checkout (git)              │
          │  Stage 2: Build (mvn compile)         │
          │  Stage 3: Prepare Test App (Tomcat)   │
          │  Stage 4: Continuous Testing          │
          │           (Selenium + JUnit)          │
          │  Stage 5: Package (mvn package)       │
          │  Stage 6: Deploy (Tomcat ROOT.war)    │
          └──────────────────────────────────────┘
                              │
          ┌───────────────────▼──────────────────┐
          │       Docker / Docker Compose         │
          │  postgres:16  ◄──► app:week11         │
          │  Port 5433         Port 8082          │
          └──────────────────────────────────────┘
                              │
          ┌───────────────────▼──────────────────┐
          │          Ansible Automation           │
          │  Provision OS, users, dirs, packages  │
          │  Validate Java 17, Maven, PostgreSQL  │
          └──────────────────────────────────────┘
```

---

## 📁 Repository Structure

```
selenium-performance-review-portal/
│
├── .env                          # Local environment variable overrides
├── .gitignore                    # Git ignore rules
├── Dockerfile                    # Docker image definition (eclipse-temurin:17-jre)
├── Dockerfile.txt                # Alternative Dockerfile reference
├── Jenkinsfile                   # Declarative Jenkins Pipeline (6 stages)
├── docker-compose.yml            # Multi-container setup: postgres + app
├── pom.xml                       # Maven build descriptor (Java 17, WAR packaging)
│
├── ansible/                      # Infrastructure-as-Code (Ansible)
│   ├── inventory.ini             # Target host: localhost (WSL/remote node)
│   ├── site.yml                  # Main provisioning playbook
│   ├── week14-provision.yml      # Week 14 automated provisioning + health checks
│   └── README.md                 # Ansible-specific documentation
│
├── docs/                         # Project documentation and diagrams
│   ├── SRS_main.pdf              # Software Requirements Specification
│   ├── Class_Diagram.png         # Class Architecture Diagram
│   ├── ER_Diagram.png            # Entity-Relationship Diagram
│   ├── System_Architecture.png   # System Architecture Overview
│   ├── UseCaseDiagram.png        # Use Case Diagram
│   ├── Jenkins.png               # Jenkins pipeline screenshot
│   ├── BuildSuccess.png          # Successful build screenshot
│   ├── ManagerDashboard.png      # Manager dashboard UI screenshot
│   ├── ReviewEvaluation.png      # Review evaluation UI screenshot
│   └── TestReport.png            # Selenium test report screenshot
│
└── src/
    ├── main/
    │   ├── java/com/review/portal/
    │   │   ├── PerformanceReviewPortalApplication.java   # Entry point
    │   │   ├── config/           # Spring Security & MVC configuration
    │   │   ├── controller/       # MVC Controllers
    │   │   │   ├── AuthController.java
    │   │   │   ├── EmployeeController.java
    │   │   │   ├── HomeController.java
    │   │   │   ├── ManagerAuthController.java
    │   │   │   ├── ManagerController.java
    │   │   │   ├── ManagerDashboardController.java
    │   │   │   ├── ManagerReviewController.java
    │   │   │   └── ReviewController.java
    │   │   ├── dto/              # Data Transfer Objects
    │   │   ├── exception/        # Custom exceptions (ResourceNotFoundException)
    │   │   ├── model/            # JPA Entities (Employee, Manager, Review, ReviewStatus)
    │   │   ├── repository/       # Spring Data JPA Repositories
    │   │   └── service/          # Business Logic (AuthService, etc.)
    │   └── resources/
    │       ├── application.properties   # All Spring Boot configuration
    │       ├── static/                  # CSS, JS, static assets
    │       └── templates/               # Thymeleaf HTML templates
    │
    └── test/
        └── java/com/review/portal/
            ├── PerformanceReviewPortalApplicationTests.java
            └── selenium/
                ├── BaseTest.java                     # WebDriver setup/teardown
                ├── LoginTest.java                    # Login flow tests
                ├── LogoutTest.java                   # Logout flow tests
                ├── DashboardTest.java                # Employee dashboard tests
                ├── SubmitReviewTest.java             # Review submission tests
                ├── ReviewHistoryTest.java            # Review history tests
                ├── ManagerAuthTest.java              # Manager auth tests
                ├── ManagerDashboardTest.java         # Manager dashboard tests
                ├── ManagerApprovalWorkflowTest.java  # Approval workflow tests
                └── EmployeeReviewWorkflowTest.java   # End-to-end review workflow
```

---

## ✅ Prerequisites

Install all of the following before running the project:

### Required Software

| Tool | Minimum Version | Purpose | Download |
|---|---|---|---|
| **Java JDK** | 17 | Compile & run the application | [adoptium.net](https://adoptium.net) |
| **Apache Maven** | 3.8+ | Build, test, package | [maven.apache.org](https://maven.apache.org) |
| **PostgreSQL** | 15 or 16 | Primary database | [postgresql.org](https://www.postgresql.org/download) |
| **Apache Tomcat** | 10.x | Application server (WAR deployment) | [tomcat.apache.org](https://tomcat.apache.org) |
| **Git** | 2.x | Source control | [git-scm.com](https://git-scm.com) |
| **Docker Desktop** | 24+ | Container runtime | [docker.com](https://www.docker.com/products/docker-desktop) |
| **Docker Compose** | v2+ | Multi-container orchestration | Bundled with Docker Desktop |
| **Jenkins** | 2.400+ | CI/CD server | [jenkins.io](https://www.jenkins.io/download) |
| **Ansible** | 2.14+ | Infrastructure automation | [docs.ansible.com](https://docs.ansible.com) |
| **Google Chrome** | Latest | Selenium browser target | [google.com/chrome](https://www.google.com/chrome) |
| **ChromeDriver** | Match Chrome version | Selenium WebDriver | [chromedriver.chromium.org](https://chromedriver.chromium.org) |

### Verify Your Installations

Run the following commands to confirm everything is installed correctly:

```bash
java -version
# Expected: openjdk version "17.x.x" ...

mvn -version
# Expected: Apache Maven 3.x.x ...

docker --version
# Expected: Docker version 24.x.x ...

docker compose version
# Expected: Docker Compose version v2.x.x

psql --version
# Expected: psql (PostgreSQL) 16.x

git --version
# Expected: git version 2.x.x

ansible --version
# Expected: ansible [core 2.x.x]

chromedriver --version
# Expected: ChromeDriver 12x.x.x.x
```

---

## 🚀 Local Development Setup

### Step 1 — Clone the Repository

```bash
git clone https://github.com/your-org/selenium-performance-review-portal.git
cd selenium-performance-review-portal
```

### Step 2 — Create the PostgreSQL Database

Open your PostgreSQL client and run:

```sql
CREATE DATABASE performance_review;
\l
```

> **Note:** `spring.jpa.hibernate.ddl-auto=update` means Hibernate will auto-create/update all tables on first startup. No manual SQL migration scripts required.

### Step 3 — Configure Environment Variables

Create or update the `.env` file in the project root:

```bash
DB_HOST=localhost
DB_PORT=5432
DB_NAME=performance_review
DB_USERNAME=postgres
DB_PASSWORD=YourSecurePassword
```

> ⚠️ **Security:** Never commit real credentials to source control. `.env` is already in `.gitignore`.

### Step 4 — Configure Tomcat (for WAR Deployment)

1. Download Apache Tomcat 10.x and extract to `C:\Tomcat` (Windows) or `/opt/tomcat` (Linux).
2. Set the `TOMCAT_HOME` environment variable:

**Windows (PowerShell):**
```powershell
$env:TOMCAT_HOME = "C:\Tomcat"
```

**Linux / macOS / WSL:**
```bash
export TOMCAT_HOME=/opt/tomcat
```

3. Verify Tomcat:
```bash
# Windows
%TOMCAT_HOME%\bin\catalina.bat version

# Linux/macOS
$TOMCAT_HOME/bin/catalina.sh version
```

---

## 🔧 Environment Variables

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `performance_review` | Database name |
| `DB_USERNAME` | `postgres` | Database user |
| `DB_PASSWORD` | `Jawadsk@12` | Database password |
| `SERVER_PORT` | `8080` | Embedded server port |

These can be overridden via `.env`, system environment variables, Docker Compose `environment:` block, or Jenkins pipeline `environment {}` block.

---

## 🔨 Building the Application

All Maven commands run from the **project root** (where `pom.xml` lives).

### Compile Only

```bash
mvn clean compile
```

Compiles Java sources into `target/classes/`. Used in the Jenkins pipeline Stage 2 to verify code compiles before further stages run.

### Package as WAR — Skip Tests

```bash
mvn clean package -DskipTests
```

Produces `target/performance-review-portal.war`. Use `-DskipTests` during packaging when Selenium tests require a live server.

### Package as WAR — With All Tests

```bash
mvn clean package
```

> ⚠️ Requires a live Tomcat instance (app deployed), running PostgreSQL, and ChromeDriver on `PATH`.

### Clean Build Output

```bash
mvn clean
```

Removes the `target/` directory.

### View Dependency Tree

```bash
mvn dependency:tree
```

### Check for Outdated Dependencies

```bash
mvn versions:display-dependency-updates
```

---

## ▶️ Running the Application

### Option A — Standalone (Embedded Tomcat)

```bash
# 1. Package
mvn clean package -DskipTests

# 2. Run on default port 8080
java -jar target/performance-review-portal.war

# 3. Run on a custom port
java -jar target/performance-review-portal.war --server.port=8082

# 4. Run with explicit DB credentials
java -jar target/performance-review-portal.war \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/performance_review \
  --spring.datasource.username=postgres \
  --spring.datasource.password=YourPassword
```

**Access:** http://localhost:8080/login

---

### Option B — Deploy WAR to External Apache Tomcat

```bash
# 1. Package the WAR
mvn clean package -DskipTests

# --- WINDOWS ---
# 2. Stop running Tomcat
%TOMCAT_HOME%\bin\shutdown.bat

# 3. Remove old deployment
rmdir /S /Q %TOMCAT_HOME%\webapps\ROOT
del /Q %TOMCAT_HOME%\webapps\ROOT.war

# 4. Copy the new WAR as ROOT.war (serves app at root context '/')
copy /Y target\performance-review-portal.war %TOMCAT_HOME%\webapps\ROOT.war

# 5. Start Tomcat
%TOMCAT_HOME%\bin\startup.bat

# --- LINUX / macOS ---
# 2. Stop Tomcat
$TOMCAT_HOME/bin/shutdown.sh

# 3. Remove old deployment
rm -rf $TOMCAT_HOME/webapps/ROOT
rm -f $TOMCAT_HOME/webapps/ROOT.war

# 4. Copy WAR
cp target/performance-review-portal.war $TOMCAT_HOME/webapps/ROOT.war

# 5. Start Tomcat
$TOMCAT_HOME/bin/startup.sh
```

**Access:** http://localhost:8081/login

---

### Option C — Maven Spring Boot Plugin (Development)

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=performance_review
export DB_USERNAME=postgres
export DB_PASSWORD=YourPassword

mvn spring-boot:run
```

**Access:** http://localhost:8080/login

---

## 🐳 Docker — Containerised Deployment

### Step 1 — Build the WAR

```bash
mvn clean package -DskipTests
```

### Step 2 — Build the Docker Image

```bash
# Build with latest tag
docker build -t performance-review-portal:latest .

# Build with week tag (used in docker-compose.yml)
docker build -t performance-review-portal:week11 .

# Verify the image was created
docker images | grep performance-review-portal
```

### Step 3 — Run the Application Container

```bash
docker run -d \
  --name performance-review-app \
  -p 8082:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=5432 \
  -e DB_NAME=performance_review \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=YourPassword \
  -e SERVER_PORT=8080 \
  performance-review-portal:latest
```

### Useful Docker Commands

```bash
# View running containers
docker ps

# View all containers (including stopped)
docker ps -a

# Follow application logs
docker logs -f performance-review-app

# Stop the container
docker stop performance-review-app

# Remove the container
docker rm performance-review-app

# Remove the image
docker rmi performance-review-portal:latest

# Enter the container's shell
docker exec -it performance-review-app /bin/bash
```

---

## 🐙 Docker Compose — Full Stack

Starts **PostgreSQL and the application** together in an isolated Docker network.

### Start the Full Stack

```bash
# Start in foreground (see all logs)
docker compose up

# Start in background (detached)
docker compose up -d

# Force rebuild of the app image and start
docker compose up --build

# Force rebuild and start in background
docker compose up --build -d
```

### Check Service Status

```bash
# View all running services
docker compose ps

# View logs for all services
docker compose logs

# Follow logs for app only
docker compose logs -f app

# Follow logs for postgres only
docker compose logs -f postgres
```

### Stop and Clean Up

```bash
# Stop all services (containers preserved)
docker compose stop

# Stop and remove containers
docker compose down

# Stop, remove containers AND volumes — WARNING: deletes all DB data
docker compose down -v

# Remove everything including images
docker compose down --rmi all -v
```

### Port Mapping

| Service | Container Port | Host Port | Access URL |
|---|---|---|---|
| PostgreSQL | 5432 | **5433** | `localhost:5433` |
| Application | 8080 | **8082** | http://localhost:8082/login |

> Host port is `5433` (not 5432) to avoid conflicts with a local PostgreSQL instance.

### Connect to PostgreSQL Inside Docker

```bash
# Via psql client
psql -h localhost -p 5433 -U postgres -d performance_review

# Via Docker exec
docker exec -it performance-review-postgres psql -U postgres -d performance_review
```

---

## 🧪 Selenium Automated Testing

The project contains **10 Selenium WebDriver test classes** for full end-to-end browser testing.

### Test Classes

| Test Class | What it Tests |
|---|---|
| `BaseTest.java` | ChromeDriver setup/teardown base class |
| `LoginTest.java` | Employee login with valid/invalid credentials |
| `LogoutTest.java` | Session termination and redirect behaviour |
| `DashboardTest.java` | Employee dashboard content validation |
| `SubmitReviewTest.java` | Review form submission flow |
| `ReviewHistoryTest.java` | Review history listing and filtering |
| `ManagerAuthTest.java` | Manager login and authentication |
| `ManagerDashboardTest.java` | Manager team dashboard view |
| `ManagerApprovalWorkflowTest.java` | Manager review approval end-to-end |
| `EmployeeReviewWorkflowTest.java` | Complete employee-side review workflow |

### Prerequisites for Selenium Tests

1. ChromeDriver must be on your system `PATH`:
```bash
chromedriver --version
```

2. The application must be **running at `http://localhost:8081/login`** before tests execute.

3. A populated PostgreSQL database must be accessible.

### Run All Tests (Unit + Selenium)

```bash
mvn test
```

### Run a Specific Test Class

```bash
mvn test -Dtest="LoginTest"
```

### Run a Specific Test Method

```bash
mvn test -Dtest="LoginTest#testValidLogin"
```

### Run Tests and Generate HTML Report

```bash
mvn test surefire-report:report
# Report: target/site/surefire-report.html
```

### Run Tests in Headless Mode (CI / No Display)

```bash
mvn test -Dwebdriver.chrome.args="--headless,--no-sandbox,--disable-dev-shm-usage"
```

### View Surefire XML Reports

```bash
# List all report files
ls target/surefire-reports/

# View summary on Linux
grep -E 'tests=|failures=|errors=' target/surefire-reports/*.xml
```

---

## 🔁 Jenkins CI/CD Pipeline

The `Jenkinsfile` in the project root defines a **6-stage declarative pipeline**.

### Pipeline Stages Overview

```
Checkout ──► Build ──► Prepare Test App ──► Continuous Testing ──► Package ──► Deploy
```

| Stage | Command | Purpose |
|---|---|---|
| **1. Checkout** | `checkout scm` | Pull latest code from Git |
| **2. Build** | `mvn clean compile` | Verify code compiles |
| **3. Prepare Test App** | `mvn package -DskipTests` + Tomcat | Deploy to Tomcat for Selenium |
| **4. Continuous Testing** | `mvn test` | Run JUnit + Selenium tests |
| **5. Package** | `mvn clean package -DskipTests` | Create final WAR |
| **6. Deploy** | Copy ROOT.war + health check | Deploy to production Tomcat |

### Step 1 — Install Jenkins

```bash
# Ubuntu / Debian
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo apt-key add -
sudo sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt update && sudo apt install -y jenkins
sudo systemctl start jenkins
sudo systemctl enable jenkins
sudo systemctl status jenkins
```

Access Jenkins at: **http://localhost:8080**

Get initial admin password:
```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

### Step 2 — Install Required Jenkins Plugins

Go to **Manage Jenkins → Plugin Manager → Available** and install:

- Pipeline (Declarative Pipeline support)
- Git (Source control integration)
- JUnit (Test report publishing)
- Maven Integration
- GitHub Integration (optional, for webhooks)

### Step 3 — Configure Maven & Java in Jenkins

Go to **Manage Jenkins → Global Tool Configuration**:

- **JDK**: Name it `JDK-17`, set `JAVA_HOME` (e.g. `C:\Program Files\Java\jdk-17`)
- **Maven**: Name it `Maven-3`, set `MAVEN_HOME`

### Step 4 — Create the Pipeline Job

1. Click **New Item** → name it `performance-review-portal`
2. Select **Pipeline** → OK
3. Under **Pipeline Definition**:
   - Definition: `Pipeline script from SCM`
   - SCM: `Git`
   - Repository URL: `https://github.com/your-org/selenium-performance-review-portal.git`
   - Branch: `*/main`
   - Script Path: `Jenkinsfile`
4. Save and click **Build Now**

### Step 5 — Trigger a Build with Environment Selection

1. Click **Build with Parameters**
2. Choose `DEPLOY_ENV`: `staging` or `production`
3. Click **Build**

### Step 6 — Set TOMCAT_HOME in Jenkins

**Option A — Edit the Jenkinsfile environment block:**
```groovy
environment {
    APP_NAME  = 'performance-review-portal'
    TOMCAT_HOME = 'C:\\Tomcat'   // Update this path
    APP_URL   = 'http://localhost:8081/login'
}
```

**Option B — Jenkins Global Environment Variable:**
- Manage Jenkins → Configure System → Global properties → Environment variables
- Add: `TOMCAT_HOME` = `C:\Tomcat`

### Step 7 — View Results

- Blue Ocean dashboard: http://localhost:8080/blue
- Stage view: click a build number → Stage View
- Console log: click a build → Console Output
- Test report: click a build → Test Result

---

## ⚙️ Ansible — Infrastructure Provisioning

Ansible playbooks automate full server environment provisioning.

### Inventory File

```ini
# ansible/inventory.ini
[performance_review_portal]
localhost ansible_connection=local
```

For a remote server, replace with:
```ini
[performance_review_portal]
192.168.1.100 ansible_user=ubuntu ansible_ssh_private_key_file=~/.ssh/id_rsa
```

### Ping Test (Verify Connectivity)

```bash
cd ansible/
ansible -i inventory.ini performance_review_portal -m ping
```

Expected output:
```
localhost | SUCCESS => {
    "changed": false,
    "ping": "pong"
}
```

---

### Main Provisioning Playbook — `site.yml`

**What it does:**
- Updates APT package cache
- Installs Java 17, Maven, Git, curl, net-tools
- Creates `performance` OS user and group
- Creates application directories (`/opt/`, `/etc/`, `/var/log/`)
- Checks Docker, PostgreSQL port, and application port availability
- Prints a full configuration summary

```bash
cd ansible/

# Dry-run — check what would change, make no changes
ansible-playbook -i inventory.ini site.yml --check

# Run the playbook
ansible-playbook -i inventory.ini site.yml

# Run with verbose output
ansible-playbook -i inventory.ini site.yml -v

# Run with extra verbose (great for debugging)
ansible-playbook -i inventory.ini site.yml -vvv

# Run with sudo password prompt
ansible-playbook -i inventory.ini site.yml --ask-become-pass

# List all tasks without executing
ansible-playbook -i inventory.ini site.yml --list-tasks

# List all target hosts
ansible-playbook -i inventory.ini site.yml --list-hosts
```

---

### Week 14 Advanced Playbook — `week14-provision.yml`

**What it does (in addition to site.yml):**
- Creates release manifest file (`release.txt`) with stable/current release metadata
- Verifies Java 17 installation
- Verifies Maven installation
- Health-checks PostgreSQL port (5432)
- Health-checks application port (8082)
- Prints a complete provisioning summary

```bash
cd ansible/

# Dry-run
ansible-playbook -i inventory.ini week14-provision.yml --check

# Full execution
ansible-playbook -i inventory.ini week14-provision.yml

# With verbose output
ansible-playbook -i inventory.ini week14-provision.yml -v

# Override variables at runtime
ansible-playbook -i inventory.ini week14-provision.yml \
  -e "app_port=9000 postgres_port=5433 current_release=week15"

# List all tasks
ansible-playbook -i inventory.ini week14-provision.yml --list-tasks
```

### Provisioning Variables Reference

| Variable | Value | Description |
|---|---|---|
| `app_name` | `performance-review-portal` | Application identifier |
| `app_user` | `performance` | OS user for the app |
| `app_group` | `performance` | OS group for the app |
| `app_root` | `/opt/performance-review-portal` | Application root |
| `config_dir` | `/etc/performance-review-portal` | Configuration directory |
| `logs_dir` | `/var/log/performance-review-portal` | Log directory |
| `deploy_dir` | `/opt/performance-review-portal/deploy` | Deployment artefacts |
| `app_port` | `8082` | Application port |
| `postgres_port` | `5432` | PostgreSQL port |
| `stable_release` | `week11` | Last stable release tag |
| `current_release` | `week14` | Current release tag |

### Verify Provisioning Results

```bash
# Check OS user was created
id performance

# Check directories exist
ls -la /opt/performance-review-portal/
ls -la /etc/performance-review-portal/
ls -la /var/log/performance-review-portal/
ls -la /opt/performance-review-portal/deploy/

# Read the release manifest
cat /opt/performance-review-portal/deploy/release.txt

# Verify installed tools
java -version
mvn -version
git --version

# Check listening ports
netstat -tlnp | grep -E "8082|5432"
```

---

## 📅 Week-by-Week DevOps Milestones

| Week | Milestone |
|---|---|
| **Week 1–3** | Project inception, SRS documentation, architecture design |
| **Week 4–5** | Spring Boot application development (Employee & Manager flows) |
| **Week 6** | PostgreSQL integration, JPA entities, repository layer |
| **Week 7** | Jenkins CI configured — first automated build + WAR packaging |
| **Week 8** | Git branching strategy, conflict resolution, feature branches |
| **Week 9** | Selenium WebDriver tests added (login, dashboard, review workflows) |
| **Week 10** | Full 6-stage Jenkins pipeline with Continuous Testing gate |
| **Week 11** | Docker containerisation — Dockerfile + Docker Compose |
| **Week 12** | Docker image tagging (`week11` release), multi-container testing |
| **Week 13** | Ansible provisioning playbook (`site.yml`) for target node setup |
| **Week 14** | Advanced Ansible automation (`week14-provision.yml`) with health checks and release manifest |

---

## 🌐 Application Endpoints & URLs

### Standalone / Tomcat Deployment (port 8081)

| Role | URL |
|---|---|
| Home / Redirect | http://localhost:8081/ |
| Employee Login | http://localhost:8081/login |
| Employee Dashboard | http://localhost:8081/dashboard |
| Submit Review | http://localhost:8081/review/submit |
| Review History | http://localhost:8081/review/history |
| Manager Login | http://localhost:8081/manager/login |
| Manager Dashboard | http://localhost:8081/manager/dashboard |
| Manager Review List | http://localhost:8081/manager/reviews |

### Docker Compose Deployment (port 8082)

Replace `8081` with `8082` in all URLs above.

### Embedded Spring Boot Dev Mode (port 8080)

Replace `8081` with `8080` in all URLs above.

---

## 🔧 Troubleshooting

### Application fails to start — PostgreSQL connection refused

```
Error: Connection refused: localhost:5432
```

**Fix:**
```bash
# Linux/macOS
sudo systemctl status postgresql
sudo systemctl start postgresql

# Windows
net start postgresql-x64-16

# Verify DB exists
psql -U postgres -l
```

---

### `mvn test` fails — ChromeDriver not found

```
Error: The path to the driver executable must be set
```

**Fix:**
```bash
# Download ChromeDriver matching your Chrome version from:
# https://chromedriver.chromium.org/downloads

# Linux — place on PATH
sudo mv chromedriver /usr/local/bin/
sudo chmod +x /usr/local/bin/chromedriver
chromedriver --version
```

---

### Jenkins Stage 3 fails — App not available on port 8081

```
ERROR: Application did not become available on port 8081.
```

**Fix:**
1. Check `TOMCAT_HOME` is set correctly in Jenkins global environment variables.
2. Verify `%TOMCAT_HOME%\bin\startup.bat` exists.
3. Check `%TOMCAT_HOME%\logs\catalina.out` for startup errors.
4. Check port availability:
   ```cmd
   netstat -aon | findstr :8081
   ```

---

### Docker Compose app container exits immediately

```
Error: Unable to connect to database
```

**Fix:** The app starts before PostgreSQL is fully ready. Restart:
```bash
docker compose down -v
docker compose up --build
```

---

### Ansible playbook fails — permission denied

```
FAILED! => {"msg": "Missing sudo password"}
```

**Fix:**
```bash
ansible-playbook -i inventory.ini site.yml --ask-become-pass
```

---

### Port 5433 already in use (Docker Compose)

```
Error: Bind for 0.0.0.0:5433 failed: port is already allocated
```

**Fix:** Edit `docker-compose.yml`:
```yaml
ports:
  - "5434:5432"
```

---

## 🤝 Contributing

1. Fork the repository.
2. Create a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Commit with conventional commit messages:
   ```bash
   git commit -m "feat: add manager notification on review approval"
   ```
4. Push and open a Pull Request:
   ```bash
   git push origin feature/your-feature-name
   ```
5. Ensure all 6 Jenkins pipeline stages pass before requesting a review.

### Commit Message Convention

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat:  add new feature
fix:   resolve a bug
chore: dependency updates, build scripts
docs:  documentation changes
ci:    changes to Jenkins/GitHub Actions config
test:  adding or updating tests
```

---

## 📜 License

This project is licensed under the **MIT License**.

---

> 💡 **Quick Start Cheatsheet**
>
> ```bash
> # 1. Clone
> git clone <repo-url> && cd selenium-performance-review-portal
>
> # 2. Build WAR
> mvn clean package -DskipTests
>
> # 3. Run full stack with Docker Compose
> docker compose up --build -d
> # Open: http://localhost:8082/login
>
> # 4. Provision server with Ansible
> cd ansible && ansible-playbook -i inventory.ini week14-provision.yml
>
> # 5. Run all Selenium tests (app must be running first)
> mvn test
> ```