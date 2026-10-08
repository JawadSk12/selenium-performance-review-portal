FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/performance-review-portal.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war", "--server.port=8080"]
