pipeline {

    agent any

    parameters {
        choice(
            name: 'DEPLOY_ENV',
            choices: ['staging', 'production'],
            description: 'Select the deployment environment'
        )
    }

    environment {
        APP_NAME = 'performance-review-portal'
        TOMCAT_HOME = 'C:\\Tomcat'
        APP_URL = 'http://localhost:8081/login'
    }

    stages {

        stage('Checkout') {
            steps {

                echo '=========================================='
                echo 'STAGE 1: CHECKOUT'
                echo '=========================================='

                checkout scm

                echo 'Source code checkout completed successfully.'
            }
        }

        stage('Build') {
            steps {

                echo '=========================================='
                echo 'STAGE 2: BUILD'
                echo '=========================================='

                bat 'mvn clean compile'

                echo 'Maven build completed successfully.'
            }
        }

        stage('Continuous Testing') {
            steps {

                echo '=========================================='
                echo 'STAGE 3: CONTINUOUS TESTING'
                echo '=========================================='

                echo 'Running unit tests and Selenium WebDriver tests...'

                bat 'mvn test'

                echo 'All tests completed successfully.'
            }

            post {

                always {

                    echo 'Publishing Maven Surefire test reports...'

                    junit(
                        testResults: 'target/surefire-reports/*.xml',
                        allowEmptyResults: false
                    )

                    echo 'Test reports published successfully.'
                }

                success {

                    echo '=========================================='
                    echo 'CONTINUOUS TESTING PASSED'
                    echo '=========================================='

                    echo 'All unit tests and Selenium tests passed.'
                    echo 'Deployment is allowed to continue.'
                }

                failure {

                    echo '=========================================='
                    echo 'CONTINUOUS TESTING FAILED'
                    echo '=========================================='

                    echo 'One or more tests failed.'
                    echo 'Deployment will be stopped.'
                }
            }
        }

        stage('Package') {
            steps {

                echo '=========================================='
                echo 'STAGE 4: PACKAGE'
                echo '=========================================='

                bat 'mvn clean package -DskipTests'

                echo 'WAR package created successfully.'
            }
        }

        stage('Deploy') {
            steps {

                echo '=========================================='
                echo 'STAGE 5: DEPLOY'
                echo '=========================================='

                echo "Application: ${env.APP_NAME}"
                echo "Deployment environment: ${params.DEPLOY_ENV}"
                echo "Tomcat location: ${env.TOMCAT_HOME}"
                echo "Application URL: ${env.APP_URL}"

                bat '''
                    echo ==========================================
                    echo CHECKING WAR FILE
                    echo ==========================================

                    if not exist "target\\%APP_NAME%.war" (
                        echo ERROR: WAR file not found.
                        exit /b 1
                    )

                    echo WAR file found successfully.

                    echo ==========================================
                    echo REMOVING PREVIOUS DEPLOYMENT
                    echo ==========================================

                    if exist "%TOMCAT_HOME%\\webapps\\%APP_NAME%" (
                        rmdir /S /Q "%TOMCAT_HOME%\\webapps\\%APP_NAME%"
                    )

                    if exist "%TOMCAT_HOME%\\webapps\\%APP_NAME%.war" (
                        del /Q "%TOMCAT_HOME%\\webapps\\%APP_NAME%.war"
                    )

                    echo Previous deployment removed.

                    echo ==========================================
                    echo COPYING NEW WAR TO TOMCAT
                    echo ==========================================

                    copy /Y "target\\%APP_NAME%.war" "%TOMCAT_HOME%\\webapps\\%APP_NAME%.war"

                    if errorlevel 1 (
                        echo ERROR: Failed to copy WAR to Tomcat.
                        exit /b 1
                    )

                    echo WAR copied successfully.

                    echo ==========================================
                    echo WAITING FOR APPLICATION
                    echo ==========================================

                    echo Waiting for the deployed application to become available...

                    powershell -NoProfile -Command "$deadline=(Get-Date).AddSeconds(90); $url='http://localhost:8081/login'; $success=$false; while ((Get-Date) -lt $deadline) { try { $response=Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 5; if ($response.StatusCode -eq 200) { $success=$true; break } } catch { }; Start-Sleep -Seconds 3 }; if (-not $success) { Write-Host 'ERROR: Application did not respond successfully on http://localhost:8081/login within 90 seconds.'; exit 1 }"

                    if errorlevel 1 (
                        echo ERROR: Application health check failed.
                        exit /b 1
                    )

                    echo ==========================================
                    echo APPLICATION HEALTH CHECK PASSED
                    echo ==========================================

                    echo Application responded successfully.
                    echo URL: http://localhost:8081/login

                    echo ==========================================
                    echo TOMCAT DEPLOYMENT SUCCESSFUL
                    echo ==========================================

                    echo Application: %APP_NAME%
                    echo Environment: %DEPLOY_ENV%
                    echo URL: http://localhost:8081/%APP_NAME%/
                '''
            }
        }
    }

    post {

        success {

            echo '=========================================='
            echo 'WEEK 10 PIPELINE SUCCESS'
            echo '=========================================='

            echo "Application: ${env.APP_NAME}"
            echo "Environment: ${params.DEPLOY_ENV}"
            echo "Tomcat: ${env.TOMCAT_HOME}"

            echo 'Checkout: SUCCESS'
            echo 'Build: SUCCESS'
            echo 'Continuous Testing: SUCCESS'
            echo 'Test Report: PUBLISHED'
            echo 'Package: SUCCESS'
            echo 'Deploy: SUCCESS'
            echo 'Application Health Check: SUCCESS'

            echo 'Application URL: http://localhost:8081/performance-review-portal/'

            echo '=========================================='
            echo 'WEEK 10 CONTINUOUS TESTING COMPLETED'
            echo '=========================================='
        }

        failure {

            echo '=========================================='
            echo 'WEEK 10 PIPELINE FAILED'
            echo '=========================================='

            echo 'One or more pipeline stages failed.'

            echo 'If Continuous Testing failed, deployment was automatically stopped.'

            echo 'Check the Jenkins console output and published test report.'

            echo '=========================================='
            echo 'FAILURE ANALYSIS'
            echo '=========================================='

            echo 'Possible failure points:'
            echo '1. Checkout'
            echo '2. Build'
            echo '3. Continuous Testing'
            echo '4. Package'
            echo '5. Deployment'
            echo '6. Application Health Check'
        }
    }
}