package com.review.portal.config;

import com.review.portal.model.Employee;
import com.review.portal.model.Manager;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;
import com.review.portal.repository.EmployeeRepository;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Enterprise Database Seeder and Bootstrap Runner.
 * Automatically seeds the PostgreSQL database with default demo accounts
 * (Jawad Shaikh as Employee, Ahmed Khan as Manager) and pending/approved reviews
 * if the database is newly initialized (e.g. in fresh Docker containers or local deployments).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseDataInitializer implements CommandLineRunner {

    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            log.info("Verifying database seed data status...");

            Manager manager = managerRepository.findByEmail("manager@gmail.com").orElse(null);
            if (manager == null) {
                log.info("Bootstrapping default Manager account (manager@gmail.com)...");
                manager = Manager.builder()
                        .name("Ahmed Khan")
                        .email("manager@gmail.com")
                        .password("1234")
                        .department("IT Management")
                        .build();
                manager = managerRepository.save(manager);
                log.info("Manager account initialized with ID: {}", manager.getId());
            }

            Employee jawad = employeeRepository.findByEmail("jawad@gmail.com").orElse(null);
            if (jawad == null) {
                log.info("Bootstrapping default Employee account (jawad@gmail.com)...");
                jawad = Employee.builder()
                        .name("Jawad Shaikh")
                        .email("jawad@gmail.com")
                        .password("1234")
                        .department("IT")
                        .designation("Software Intern")
                        .employeeCode("EMP-1001")
                        .manager(manager)
                        .build();
                jawad = employeeRepository.save(jawad);
                log.info("Employee account initialized with ID: {}", jawad.getId());
            }

            // Seed other colleagues if they do not exist
            if (!employeeRepository.existsByEmail("aisha.patel@company.com")) {
                Employee aisha = Employee.builder()
                        .name("Aisha Patel")
                        .email("aisha.patel@company.com")
                        .password("1234")
                        .department("Cloud Infrastructure")
                        .designation("Cloud Solutions Engineer")
                        .employeeCode("EMP-1002")
                        .manager(manager)
                        .build();
                employeeRepository.save(aisha);
            }

            if (!employeeRepository.existsByEmail("carlos.r@company.com")) {
                Employee carlos = Employee.builder()
                        .name("Carlos Rodriguez")
                        .email("carlos.r@company.com")
                        .password("1234")
                        .department("Frontend & Mobile")
                        .designation("Senior UI/UX Engineer")
                        .employeeCode("EMP-1003")
                        .manager(manager)
                        .build();
                employeeRepository.save(carlos);
            }

            // Ensure initial reviews exist for dashboard visualization
            if (reviewRepository.count() == 0 && jawad != null) {
                log.info("Bootstrapping demo review records for evaluation and metrics...");

                Review review1 = Review.builder()
                        .employee(jawad)
                        .manager(manager)
                        .technical(9)
                        .communication(9)
                        .teamwork(8)
                        .problemSolving(10)
                        .achievement("Architected CI/CD pipelines and automated Selenium testing suite with quality gates.")
                        .futureGoal("Implement Kubernetes orchestration, GitOps canary deployments, and SRE observability.")
                        .overallScore(9.0)
                        .status(ReviewStatus.PENDING)
                        .reviewDate(LocalDate.now())
                        .build();
                reviewRepository.save(review1);

                Review review2 = Review.builder()
                        .employee(jawad)
                        .manager(manager)
                        .technical(9)
                        .communication(8)
                        .teamwork(9)
                        .problemSolving(9)
                        .achievement("Migrated legacy application workflows to high-performance Spring Boot 3 & PostgreSQL.")
                        .futureGoal("Attain AWS Certified DevOps Engineer certification.")
                        .overallScore(8.75)
                        .status(ReviewStatus.APPROVED)
                        .reviewDate(LocalDate.now().minusDays(14))
                        .approvedDate(LocalDate.now().minusDays(12))
                        .managerComment("Outstanding technical execution and exemplary adherence to agile CI/CD practices.")
                        .build();
                reviewRepository.save(review2);

                log.info("Demo review records seeded successfully.");
            }

            log.info("Database verification and bootstrapping completed successfully.");
        } catch (Exception ex) {
            log.warn("Database initialization notice: {}", ex.getMessage());
        }
    }
}

