package com.review.portal.service.impl;

import com.review.portal.dto.LoginRequestDTO;
import com.review.portal.exception.BadRequestException;
import com.review.portal.model.Employee;
import com.review.portal.repository.EmployeeRepository;
import com.review.portal.service.AuthService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of AuthService for employee authentication and session lifecycle management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    public static final String SESSION_EMPLOYEE_ID = "employeeId";
    public static final String SESSION_EMPLOYEE_NAME = "employeeName";
    public static final String SESSION_EMPLOYEE_EMAIL = "employeeEmail";
    public static final String SESSION_EMPLOYEE_DEPT = "employeeDept";
    public static final String SESSION_EMPLOYEE_ROLE = "employeeRole";

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(readOnly = true)
    public Employee authenticate(LoginRequestDTO loginRequest, HttpSession session) {
        log.info("Attempting authentication for email: {}", loginRequest.getEmail());

        String email = loginRequest.getEmail() != null ? loginRequest.getEmail().trim().toLowerCase() : "";
        String rawPassword = loginRequest.getPassword();

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Authentication failed: Employee with email '{}' not found", email);
                    return new BadRequestException("Invalid email or password");
                });

        // Verify password using BCrypt (with fallback for legacy plain text if any)
        boolean passwordMatches = passwordEncoder.matches(rawPassword, employee.getPassword())
                || rawPassword.equals(employee.getPassword());

        if (!passwordMatches) {
            log.warn("Authentication failed: Invalid credentials for email '{}'", email);
            throw new BadRequestException("Invalid email or password");
        }

        // Establish session state
        session.setAttribute(SESSION_EMPLOYEE_ID, employee.getId());
        session.setAttribute(SESSION_EMPLOYEE_NAME, employee.getName());
        session.setAttribute(SESSION_EMPLOYEE_EMAIL, employee.getEmail());
        session.setAttribute(SESSION_EMPLOYEE_DEPT, employee.getDepartment());
        session.setAttribute(SESSION_EMPLOYEE_ROLE, employee.getDesignation());

        if (loginRequest.isRememberMe()) {
            // Extend session lifetime to 7 days
            session.setMaxInactiveInterval(7 * 24 * 60 * 60);
        } else {
            // Standard 30 minute session
            session.setMaxInactiveInterval(30 * 60);
        }

        log.info("Authentication successful for employee ID: {} ({})", employee.getId(), employee.getEmail());
        return employee;
    }

    @Override
    public void logout(HttpSession session) {
        if (session != null) {
            Object empId = session.getAttribute(SESSION_EMPLOYEE_ID);
            log.info("Invalidating session for employee ID: {}", empId);
            session.invalidate();
        }
    }

    @Override
    public boolean isAuthenticated(HttpSession session) {
        return session != null && session.getAttribute(SESSION_EMPLOYEE_ID) != null;
    }

    /**
     * Initializes default demo employee credentials if database table is empty.
     */
    @PostConstruct
    @Transactional
    public void seedInitialDemoUser() {
        try {
            if (employeeRepository.count() == 0) {
                log.info("Seeding initial default employee into PostgreSQL database...");
                Employee defaultEmployee = Employee.builder()
                        .employeeCode("EMP-1001")
                        .name("Alex Morgan")
                        .email("alex.morgan@company.com")
                        .password(passwordEncoder.encode("Password123!"))
                        .department("Engineering")
                        .designation("Senior Software Engineer")
                        .build();

                employeeRepository.save(defaultEmployee);
                log.info("Default employee seeded: alex.morgan@company.com / Password123!");
            }
        } catch (Exception ex) {
            log.warn("Seed check skipped or deferred until database connection: {}", ex.getMessage());
        }
    }
}
