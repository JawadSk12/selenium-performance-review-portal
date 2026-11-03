@echo off
setlocal
cd /d "C:\PROJECT\selenium-performance-review-portal-main\selenium-performance-review-portal-main"

echo [1/4] Staging all new and modified files...
git add docker-compose.yml
git add seed-data.sql
git add generate_docs.py
git add src/main/java/com/review/portal/controller/AuthController.java
git add src/main/java/com/review/portal/controller/ManagerAuthController.java
git add src/main/java/com/review/portal/controller/ManagerDashboardController.java
git add src/main/java/com/review/portal/config/DatabaseDataInitializer.java
git add src/main/resources/templates/login.html
git add src/main/resources/templates/manager-login.html
git add docs/DEVOPS_15_WEEKS_MASTER_PORTFOLIO.md
git add docs/weekly-reports/

echo.
echo [2/4] Staged files:
git status --short

echo.
echo [3/4] Committing...
git commit -m "feat: fix manager-login routing, add DB auto-seeder, and generate 15-week DevOps portfolio docs"

echo.
echo [4/4] Pushing to origin/develop...
git push origin develop

echo.
echo ==== DONE ====

