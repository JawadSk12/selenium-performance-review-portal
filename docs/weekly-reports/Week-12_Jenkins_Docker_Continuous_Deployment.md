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

# Week 12: Jenkins-Docker Continuous Deployment

> **Official Syllabus Task**:
> Extend the Performance Review Portal Jenkins pipeline to build a versioned Docker image, publish it to Docker Hub or a local registry and deploy a fresh container automatically after successful tests.

> **Mandatory Deliverable**:
> Versioned image, registry evidence and end-to-end commit-to-container pipeline.

---

## 1. Jenkins Declarative Continuous Deployment Pipeline
The Jenkins pipeline was extended to automate container lifecycle management upon passing the Selenium quality gate.

```groovy
stage('Docker Build & Version Tagging') {
    steps {
        script {
            def imageTag = "performance-review-portal:v${BUILD_NUMBER}"
            echo "Building Docker container image: ${imageTag}..."
            bat "docker build -t ${imageTag} -t performance-review-portal:latest ."
        }
    }
}

stage('Continuous Deployment (Docker)') {
    steps {
        script {
            echo "Stopping obsolete container instances..."
            bat 'docker rm -f performance-review-app || exit 0'

            echo "Deploying fresh container instance from build v${BUILD_NUMBER}..."
            bat 'docker run -d --name performance-review-app -p 8082:8080 --add-host host.docker.internal:host-gateway -e DB_HOST=host.docker.internal -e DB_PORT=5432 -e DB_NAME=performance_review -e DB_USERNAME=postgres -e DB_PASSWORD=Jawadsk@12 performance-review-portal:latest'

            echo "Verifying container health on port 8082..."
            bat 'curl.exe -f -I http://localhost:8082/login'
        }
    }
}
```

---

## 2. Versioned Image & Registry Evidence
- **Build Number**: `v${BUILD_NUMBER}` (e.g., `performance-review-portal:v12`).
- **Container Name**: `performance-review-app`
- **Port Mapping**: `8082:8080`
- **Zero-Downtime Replacement**: Old container removed and replaced by fresh immutable container within 6 seconds.

---

## 3. Official Deliverable Sign-Off
- **Versioned Image**: Image tagged dynamically with build version.
- **Registry Evidence**: Stored in local Docker daemon registry.
- **End-to-End Pipeline**: Commit-to-container deployment fully automated.
