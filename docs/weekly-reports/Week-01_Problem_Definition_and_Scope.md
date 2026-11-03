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

# Week 01: Problem Definition and Scope

> **Official Syllabus Task**:
> Study the real-time need for the Performance Review Portal. Identify target users, existing pain points, stakeholders, constraints and measurable success criteria. Freeze a small 15-week MVP scope.

> **Mandatory Deliverable**:
> Problem statement, stakeholder list, objectives, constraints and approved MVP scope.

---

## 1. Executive Problem Statement
In traditional modern enterprises, annual and quarterly employee performance appraisals frequently suffer from significant systemic bottlenecks:
- **Scattered Data & Spreadsheet Chaos**: Evaluation forms, self-assessments, and managerial feedback are fragmented across emails, spreadsheets, and disconnected document repositories.
- **Opacity & Delayed Feedback Loops**: Employees often spend weeks waiting for feedback, resulting in poor visibility into performance ratings, career growth milestones, and KPIs.
- **Absence of Continuous Quality Validation**: Legacy review applications lack automated regression testing and automated deployment mechanisms, leading to frequent downtime and release regressions during peak appraisal cycles.

The **Selenium-Tested Performance Review Portal** is conceived as a lightweight, enterprise-grade web application engineered to digitize, accelerate, and automate the end-to-end performance evaluation lifecycle while adhering strictly to contemporary **DevOps culture, automated quality gates, and continuous delivery**.

---

## 2. Target Users & Stakeholder Matrix

| Stakeholder Group | Primary Responsibilities & Pain Points Addressed | System Interaction |
| :--- | :--- | :--- |
| **Employees** | Submits annual self-appraisals (Technical, Communication, Teamwork, Problem-Solving), documents achievements, tracks review status in real-time. | Accesses `/login`, `/dashboard`, `/review`, `/review/history`. |
| **Engineering Managers** | Evaluates pending employee submissions, inputs qualitative feedback, assigns final ratings, and accesses team performance metrics. | Accesses `/manager-login`, `/manager/dashboard`, `/manager/review/{id}`. |
| **HR & Executive Leadership** | Needs aggregated department averages, grade distributions (Outstanding, Excellent, Good), and historical compliance audits. | Summary metrics on Executive Dashboard. |
| **DevOps / Release Engineers** | Requires reproducible builds, automated quality gates via headless Selenium suites, containerization, and configuration management. | Manages Jenkins pipelines, Docker builds, and Ansible playbooks. |

---

## 3. Project Objectives & Measurable Success Criteria

### Primary Objectives:
1. Deliver an intuitive role-based portal for Employee self-assessment and Manager evaluation.
2. Embed an automated Selenium WebDriver regression test suite to enforce a Zero-Defect Quality Gate.
3. Establish a continuous delivery pipeline (Jenkins CI/CD) deploying to both Apache Tomcat 10 and Docker containers.
4. Automate infrastructure configuration and idempotency validation using Ansible.

### Measurable Success Criteria:
- **Build & Test Cycle Time**: Under 3 minutes for complete clean compile, unit tests, and headless browser tests.
- **Quality Gate Stability**: 100% pass rate on critical user journeys (Login, Review Submission, History, Dashboard, Logout).
- **Deployment Reliability**: Zero manual interventions from Git commit to staging container runtime.
- **Zero Port Collisions**: Dedicated, non-conflicting port topology (8081 for Tomcat, 8082 for Docker, 5432/5433 for PostgreSQL).

---

## 4. Architectural Constraints & Technology Assumptions
- **Programming Language**: Java 17 (LTS) for modern language ergonomics and long-term enterprise stability.
- **Framework**: Spring Boot 3.3.3 (Web, Data JPA, Validation, Thymeleaf templating).
- **Relational Storage**: PostgreSQL 16 with strict foreign key constraints and transactional integrity.
- **Testing Engine**: Selenium WebDriver 4.24 with ChromeDriver executed in headless mode within CI/CD.
- **Build Automation**: Apache Maven 3.9+ with automated Surefire test reporting.
- **Budget**: 100% Open Source tooling with zero external licensing overhead.

---

## 5. Approved 15-Week MVP Scope Baseline

```
+---------------------------------------------------------------------------------+
|                       APPROVED 15-WEEK MVP SCOPE BASELINE                      |
+---------------------------------------------------------------------------------+
|  Core Functional Scope:                                                         |
|    [x] Secure Authentication & Role Separation (Employee vs Manager)             |
|    [x] Employee Self-Appraisal Creation & Dynamic Score Computation             |
|    [x] Historical Performance Tracking & Badge-based Status Filters             |
|    [x] Manager Evaluation Workspace, Scoring Engine, & Rejection/Approval Flows  |
|    [x] Real-time Departmental Metrics & Analytics Dashboard                     |
|                                                                                 |
|  DevOps & Quality Engineering Scope:                                            |
|    [x] Git GitFlow Branching Strategy with Conflict Resolution Evidence         |
|    [x] Jenkins Declarative Pipeline as Code (Jenkinsfile)                       |
|    [x] Selenium Automated UI Regression Test Suite (5 Core User Journeys)       |
|    [x] Quality Gate Pipeline Breakage & Defect Rectification Demonstration      |
|    [x] Docker Multi-Stage Containerization & Jenkins Continuous Deployment      |
|    [x] Ansible Configuration Management, Idempotency & Rollback Demonstration   |
+---------------------------------------------------------------------------------+
```

---

## 6. Official Deliverable Sign-Off
- **Problem Statement**: Complete and validated against enterprise performance appraisal bottlenecks.
- **Stakeholder List**: Defined with clear role-based access requirements.
- **Objectives & Constraints**: Documented with measurable success criteria.
- **Approved MVP Scope**: Frozen for 15-week execution.
