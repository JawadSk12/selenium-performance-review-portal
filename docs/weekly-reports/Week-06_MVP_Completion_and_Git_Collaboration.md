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

# Week 06: MVP Completion and Git Collaboration

> **Official Syllabus Task**:
> Implement the remaining essential MVP functions for the Performance Review Portal: create, view, update and search records; role-based status workflow; summary dashboard. Demonstrate a second feature branch, conflict creation/resolution, tagging and a release-ready source baseline.

> **Mandatory Deliverable**:
> Functional MVP, resolved merge conflict, tagged version and updated backlog.

---

## 1. Remaining MVP Functions Completed
1. **Manager Evaluation Queue & Decision Engine**: Managers review pending submissions and set final ratings, approval status, and qualitative feedback.
2. **Review History Ledger with Badges**: Employees view submitted reviews with status badges (`PENDING`, `APPROVED`, `REJECTED`).
3. **Executive HR Summary Dashboard**: Computes total reviews, pending evaluations, approved count, and departmental average score.
4. **Search & Status Filtering**: Real-time filtering across review tables.

---

## 2. Git Merge Conflict Simulation & Resolution Demonstration

To demonstrate collaborative Git version control, two concurrent branches modified the review status handling logic:
- **Branch A (`feature/manager-approval-v1`)**: Set approval status as uppercase string literal `"APPROVED"`.
- **Branch B (`feature/manager-approval-enum`)**: Refactored status to use type-safe `ReviewStatus.APPROVED` enum.

### Git Conflict Markers Encountered:
```diff
<<<<<<< HEAD (develop)
        review.setStatus("APPROVED");
        review.setApprovedDate(LocalDate.now());
=======
        review.setStatus(ReviewStatus.APPROVED);
        review.setApprovedDate(LocalDate.now());
        review.setManagerComment(evaluationDTO.getComments());
>>>>>>> feature/manager-approval-enum
```

### Resolution Strategy:
The conflict was resolved in favor of the type-safe `ReviewStatus` enum while preserving the manager comments and approval date timestamp:
```java
review.setStatus(ReviewStatus.APPROVED);
review.setApprovedDate(LocalDate.now());
review.setManagerComment(evaluationDTO.getComments());
```

### Completion Commands:
```bash
git add src/main/java/com/review/portal/service/impl/ReviewServiceImpl.java
git commit -m "merge: resolve ReviewStatus enum conflict between develop and feature/manager-approval-enum"
git push origin develop
```

---

## 3. Release Baseline Tagging

```bash
# Tag the functional MVP baseline
git tag -a v1.0.0-mvp -m "Release v1.0.0: Functional MVP baseline with complete review lifecycle"
git push origin v1.0.0-mvp
```

---

## 4. Official Deliverable Sign-Off
- **Functional MVP**: All core CRUD and workflow operations validated.
- **Resolved Merge Conflict**: Conflict created, analyzed, and cleanly resolved.
- **Tagged Version**: `v1.0.0-mvp` pushed upstream to GitHub.
- **Updated Backlog**: Sprints 1–3 backlog items transitioned to **Done**.
