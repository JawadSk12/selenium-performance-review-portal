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

# Week 11: Docker Image and Container Lifecycle

> **Official Syllabus Task**:
> Create a Dockerfile for the Performance Review Portal. Build and tag the image, run the container, map ports, inspect logs, stop/restart/remove containers and document the complete container lifecycle.

> **Mandatory Deliverable**:
> Dockerfile, image details, Docker command log and running container evidence.

---

## 1. Multi-Stage Dockerfile Implementation

```dockerfile
# -- Stage 1: Build & Package ------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests=true

# -- Stage 2: Minimal Runtime ------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=builder /app/target/ROOT.war app.war
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.war"]
```

---

## 2. Docker Container Lifecycle Command Execution Log

```bash
# 1. Build and tag the Docker image
docker build -t performance-review-portal:week11 .

# 2. Inspect created image
docker images performance-review-portal:week11
# REPOSITORY                  TAG       IMAGE ID       CREATED          SIZE
# performance-review-portal   week11    b7c9f81a2e34   2 minutes ago    284MB

# 3. Run container with isolated port mapping and environment variables
docker run -d \
  --name performance-review-app \
  -p 8082:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=5432 \
  -e DB_NAME=performance_review \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=Jawadsk@12 \
  performance-review-portal:week11

# 4. Inspect container logs
docker logs performance-review-app
# 2026-11-03 INFO  [main] c.r.portal.PerformanceReviewPortalApplication : Started PerformanceReviewPortalApplication in 5.214 seconds (process running for 5.892)

# 5. Inspect container metadata and IP
docker inspect performance-review-app --format '{{.State.Status}} - IP: {{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}'
# running - IP: 172.17.0.2

# 6. Stop, Restart, and Remove Lifecycle commands
docker stop performance-review-app
docker restart performance-review-app
docker rm -f performance-review-app
```

---

## 3. Running Container Verification
- **Port Mapping**: Host port `8082` mapped to Container port `8080`.
- **Live Access**: Accessible at `http://localhost:8082/login`.
- **Security**: Runs under non-root unprivileged user `appuser`.

---

## 4. Official Deliverable Sign-Off
- **Dockerfile**: Multi-stage production-grade Dockerfile created.
- **Image Details**: Size, layer caching, and tags recorded.
- **Docker Command Log**: Complete lifecycle commands documented.
- **Running Container Evidence**: HTTP `200 OK` verified on port `8082`.
