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

# Week 04: Git and GitHub Repository Initialization

> **Official Syllabus Task**:
> Create a GitHub repository for the Performance Review Portal. Add README, .gitignore, folder structure, issue templates and branch naming rules. Commit the initial application skeleton using meaningful commit messages.

> **Mandatory Deliverable**:
> Repository URL, README, initial commits, issues and branch policy.

---

## 1. GitHub Repository Provisioning & Structure
- **Repository URL**: `https://github.com/jawadsk/selenium-performance-review-portal.git`
- **Primary Upstream Remote**: `origin`
- **Default Production Branch**: `main`
- **Active Integration Branch**: `develop`

```
selenium-performance-review-portal/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.md
│   │   └── feature_request.md
│   └── PULL_REQUEST_TEMPLATE.md
├── src/
│   ├── main/
│   │   ├── java/com/review/portal/
│   │   └── resources/
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/com/review/portal/
│           └── selenium/
├── ansible/
├── docs/
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── pom.xml
└── README.md
```

---

## 2. Git Configuration & Repository Initialization Commands

```bash
# 1. Initialize local Git repository
git init -b main

# 2. Add remote origin
git remote add origin https://github.com/jawadsk/selenium-performance-review-portal.git

# 3. Create .gitignore for Maven, Java, and IDE artifacts
cat << 'EOF' > .gitignore
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr
out/

### Logs & Environment ###
*.log
.env
EOF

# 4. Create initial skeleton commit
git add .
git commit -m "chore: initial project skeleton with Spring Boot 3, Maven, and DevOps scaffolding"

# 5. Create develop branch and publish upstream
git checkout -b develop
git push -u origin main
git push -u origin develop
```

---

## 3. Branching Naming Policy & Contribution Workflow

```
main (Production Releases, tagged e.g., v1.0.0)
  ^
  | (PR after QA Gate approval)
develop (Ongoing Sprint Integration)
  ^
  |-- feature/<issue-id>-<short-description>  (e.g., feature/PRP-101-manager-scoring)
  |-- bugfix/<issue-id>-<short-description>   (e.g., bugfix/PRP-204-login-alert-selector)
  |-- hotfix/<short-description>              (directly off main for urgent production patches)
```

### Commit Message Convention:
- `feat`: New user-facing functionality.
- `fix`: Defect resolution or bug correction.
- `chore`: Build tooling, dependencies, or documentation updates.
- `test`: Unit, integration, or Selenium test suite additions.

---

## 4. Official Deliverable Sign-Off
- **Repository URL**: Established and linked upstream.
- **README & .gitignore**: Initialized and tracking clean repository state.
- **Initial Commits**: Committed with standardized conventional commit messages.
- **Branch Policy & Templates**: Branching workflow and issue templates active.
