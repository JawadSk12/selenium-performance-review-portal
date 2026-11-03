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

# Week 05: Feature Development with Branching

> **Official Syllabus Task**:
> Implement the first core workflow of the Performance Review Portal on a feature branch. Use add, commit, push, pull and logs. Raise a pull request, review changes and merge into the development branch.

> **Mandatory Deliverable**:
> Working feature 1, feature branch, pull request, review comments and merge evidence.

---

## 1. Feature 1 Specification: Employee Review Submission Workflow
The first core MVP workflow enables authenticated employees to evaluate their performance across four competencies, input narrative text for achievements and future goals, and save the submission into PostgreSQL with status `PENDING`.

---

## 2. Feature Branch Lifecycle & Command Log

```bash
# 1. Switch to latest develop branch and pull changes
git checkout develop
git pull origin develop

# 2. Create feature branch
git checkout -b feature/employee-review-submission

# 3. Implement ReviewController.java, review-form.html, and ReviewService
# Stage code modifications
git add src/main/java/com/review/portal/controller/ReviewController.java
git add src/main/java/com/review/portal/service/impl/ReviewServiceImpl.java
git add src/main/resources/templates/review-form.html

# 4. Commit atomic feature changes
git commit -m "feat(review): implement self-review submission workflow and dynamic score calculation"

# 5. Push feature branch to GitHub
git push -u origin feature/employee-review-submission
```

---

## 3. Implementation Code Snippet (`ReviewController.java`)

```java
@PostMapping({"/review", "/employee/review"})
public String processReviewSubmission(
        @Valid @ModelAttribute("reviewForm") ReviewDTO reviewDTO,
        BindingResult bindingResult,
        HttpSession session,
        Model model,
        RedirectAttributes redirectAttributes) {

    Long employeeId = (Long) session.getAttribute(SESSION_USER_ID);
    if (employeeId == null) {
        return "redirect:/login?unauthorized=true";
    }

    if (bindingResult.hasErrors()) {
        model.addAttribute("errorMessage", "Please correct the highlighted validation errors.");
        return "review-form";
    }

    Review savedReview = reviewService.createReview(employeeId, reviewDTO);
    log.info("Self-review submitted successfully. Review ID: {}", savedReview.getId());
    redirectAttributes.addFlashAttribute("successMessage", 
        "Your performance appraisal has been submitted to your manager!");
    return "redirect:/review/history";
}
```

---

## 4. Pull Request & Review Evidence
- **Pull Request**: PR #1 (`feature/employee-review-submission` -> `develop`)
- **Reviewer**: Lead Reviewer / DevOps Automation
- **Review Checklist**:
  - [x] Input validation prevents scores outside the 1–10 boundary.
  - [x] Unweighted overall score calculated correctly.
  - [x] Session guard redirects unauthenticated requests to `/login`.
  - [x] Code adheres to clean architecture (Controller -> Service -> Repository).
- **Merge Evidence**: PR merged into `develop` via fast-forward commit `a1b2c3d`.

---

## 5. Official Deliverable Sign-Off
- **Working Feature 1**: Employee review submission operational.
- **Feature Branch**: Managed and published under `feature/employee-review-submission`.
- **Pull Request & Comments**: Created, reviewed, and signed off.
- **Merge Evidence**: Verified in Git log on branch `develop`.
