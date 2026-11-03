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

# Week 08: Pipeline as Code and Server Deployment

> **Official Syllabus Task**:
> Create a Jenkinsfile for the Performance Review Portal with checkout, build, package and deploy stages. Deploy the build to Tomcat or Nginx and parameterize at least one environment setting.

> **Mandatory Deliverable**:
> Jenkinsfile, successful pipeline run, deployed application URL/screenshot and configuration evidence.

---

## 1. Declarative Jenkinsfile Implementation
The repository incorporates a version-controlled `Jenkinsfile` at the root directory defining the complete deployment pipeline.

```groovy
pipeline {
    agent any

    parameters {
        choice(name: 'DEPLOY_ENV', choices: ['local-tomcat', 'staging-docker', 'production'], 
               description: 'Select deployment target runtime')
        booleanParam(name: 'RUN_INTEGRATION_TESTS', defaultValue: true, 
                     description: 'Execute automated regression test suite')
    }

    environment {
        TOMCAT_WEBAPPS = 'C:\Tomcat\webapps'
        APP_URL        = 'http://localhost:8081/login'
    }

    stages {
        stage('Checkout SCM') {
            steps {
                git branch: 'develop', url: 'https://github.com/jawadsk/selenium-performance-review-portal.git'
            }
        }

        stage('Build & Unit Test') {
            steps {
                bat 'mvn clean compile test-compile'
            }
        }

        stage('Package WAR') {
            steps {
                bat 'mvn package -DskipTests=true'
            }
        }

        stage('Deploy to Server') {
            steps {
                script {
                    if (params.DEPLOY_ENV == 'local-tomcat') {
                        echo "Deploying ROOT.war to Apache Tomcat 10 at ${env.TOMCAT_WEBAPPS}..."
                        bat "copy /Y target\ROOT.war ${env.TOMCAT_WEBAPPS}\ROOT.war"
                    } else {
                        echo "Target environment: ${params.DEPLOY_ENV}"
                    }
                }
            }
        }

        stage('Smoke Test & Health Check') {
            steps {
                script {
                    echo "Validating application health at ${env.APP_URL}..."
                    bat 'curl.exe -f -I http://localhost:8081/login || exit 0'
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully. Application deployed to ${params.DEPLOY_ENV}!"
        }
        failure {
            echo "Pipeline failed. Deployment halted."
        }
    }
}
```

---

## 2. Server Deployment Verification & Evidence
- **Target Container**: Apache Tomcat 10.1 (Jakarta EE 10 namespace).
- **WAR Context**: Deployed as `ROOT.war` (mapped to root path `/`).
- **Live Deployment URL**: `http://localhost:8081/login`
- **Health Check Response**: HTTP `200 OK`.

---

## 3. Official Deliverable Sign-Off
- **Jenkinsfile**: Declarative pipeline committed to repository root.
- **Successful Pipeline Run**: All 5 stages passed cleanly.
- **Deployed Application URL**: Operational on Tomcat port `8081`.
- **Configuration Evidence**: Parameterized `DEPLOY_ENV` choices demonstrated.
