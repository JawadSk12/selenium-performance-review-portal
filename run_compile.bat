@echo off
setlocal
cd /d "C:\PROJECT\selenium-performance-review-portal-main\selenium-performance-review-portal-main"
"C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2023.1\plugins\maven\lib\maven3\bin\mvn.cmd" compile 2>&1
echo.
echo ==== EXIT CODE: %ERRORLEVEL% ====
