# ============================================================
#  JAWAD SHAIKH | 23102B0009 — Complete Git Commit Commands
#  Run these sequentially in your PowerShell terminal
#  (in project root: C:\PROJECT\selenium-performance-review-portal-main\...)
# ============================================================

# STEP 1 — Stage all new & changed files
git add docker-compose.yml
git add seed-data.sql
git add src/main/java/com/review/portal/controller/AuthController.java
git add src/main/java/com/review/portal/controller/ManagerAuthController.java
git add src/main/java/com/review/portal/controller/ManagerDashboardController.java
git add src/main/java/com/review/portal/config/DatabaseDataInitializer.java
git add src/main/resources/templates/login.html
git add src/main/resources/templates/manager-login.html
git add docs/
git add generate_docs.py

# STEP 2 — Confirm what's staged
git status

# STEP 3 — Commit with message
git commit -m "feat: resolve manager-login routing, add auto-seeder, generate 15-week DevOps portfolio docs"

# STEP 4 — Push to origin/develop
git push origin develop

