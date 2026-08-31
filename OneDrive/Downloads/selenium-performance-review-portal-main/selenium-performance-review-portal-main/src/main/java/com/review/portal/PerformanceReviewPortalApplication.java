package com.review.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Entry Point for the Selenium-Tested Performance Review Portal Application.
 * Configured with Spring Boot 3.x, Spring Data JPA, and MVC Architecture.
 */
@SpringBootApplication
public class PerformanceReviewPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformanceReviewPortalApplication.class, args);
    }
}
