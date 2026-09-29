package com.review.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main Entry Point for the Selenium-Tested Performance Review Portal Application.
 * Configured with Spring Boot 3.x, Spring Data JPA, and MVC Architecture.
 *
 * Week 7: Extends SpringBootServletInitializer to support WAR deployment on external Tomcat.
 * The configure() method enables Tomcat to bootstrap the Spring context from this WAR.
 */
@SpringBootApplication
public class PerformanceReviewPortalApplication extends SpringBootServletInitializer {

    /**
     * Required for external Tomcat WAR deployment (Week 7 CI/CD pipeline).
     * Tells Tomcat which Spring Boot application to initialise from this WAR.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PerformanceReviewPortalApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PerformanceReviewPortalApplication.class, args);
    }
}
