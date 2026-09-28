package com.review.portal.service.impl;

import com.review.portal.dto.LoginRequestDTO;
import com.review.portal.exception.BadRequestException;
import com.review.portal.model.Manager;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.service.ManagerAuthService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of ManagerAuthService for manager authentication and session management.
 * Completely independent of employee authentication workflows.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerAuthServiceImpl implements ManagerAuthService {

    public static final String SESSION_MANAGER_ID = "managerId";
    public static final String SESSION_MANAGER_NAME = "managerName";
    public static final String SESSION_MANAGER_EMAIL = "managerEmail";

    private final ManagerRepository managerRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(readOnly = true)
    public Manager authenticate(LoginRequestDTO loginRequest, HttpSession session) {
        log.info("Attempting manager authentication for email: {}", loginRequest.getEmail());

        String email = loginRequest.getEmail() != null ? loginRequest.getEmail().trim().toLowerCase() : "";
        String rawPassword = loginRequest.getPassword();

        Manager manager = managerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Manager authentication failed: Manager with email '{}' not found", email);
                    return new BadRequestException("Invalid email or password");
                });

        // Verify password using BCrypt or plain text fallback
        boolean passwordMatches = passwordEncoder.matches(rawPassword, manager.getPassword())
                || rawPassword.equals(manager.getPassword());

        if (!passwordMatches) {
            log.warn("Manager authentication failed: Invalid credentials for email '{}'", email);
            throw new BadRequestException("Invalid email or password");
        }

        // Establish manager session state
        session.setAttribute(SESSION_MANAGER_ID, manager.getId());
        session.setAttribute(SESSION_MANAGER_NAME, manager.getName());
        session.setAttribute(SESSION_MANAGER_EMAIL, manager.getEmail());

        if (loginRequest.isRememberMe()) {
            session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days
        } else {
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
        }

        log.info("Manager authentication successful for: {} (ID: {})", manager.getName(), manager.getId());
        return manager;
    }

    @Override
    public void logout(HttpSession session) {
        if (session != null) {
            Object mgrId = session.getAttribute(SESSION_MANAGER_ID);
            log.info("Invalidating manager session for manager ID: {}", mgrId);
            session.removeAttribute(SESSION_MANAGER_ID);
            session.removeAttribute(SESSION_MANAGER_NAME);
            session.removeAttribute(SESSION_MANAGER_EMAIL);
            session.invalidate();
        }
    }

    @Override
    public boolean isAuthenticated(HttpSession session) {
        return session != null && session.getAttribute(SESSION_MANAGER_ID) != null;
    }

    @PostConstruct
    @Transactional
    public void seedInitialDemoManager() {
        try {
            if (managerRepository.count() == 0) {
                log.info("Seeding initial demo manager into PostgreSQL database...");
                Manager defaultManager = Manager.builder()
                        .name("Ahmed Khan")
                        .email("manager@gmail.com")
                        .password("1234")
                        .department("IT")
                        .build();
                managerRepository.save(defaultManager);
                log.info("Demo manager seeded: manager@gmail.com / 1234");
            }
        } catch (Exception ex) {
            log.warn("Manager seed skipped: {}", ex.getMessage());
        }
    }
}
