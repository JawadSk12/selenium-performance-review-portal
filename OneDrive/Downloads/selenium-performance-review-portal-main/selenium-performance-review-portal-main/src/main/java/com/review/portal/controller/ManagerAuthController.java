package com.review.portal.controller;

import com.review.portal.dto.LoginRequestDTO;
import com.review.portal.model.Manager;
import com.review.portal.service.ManagerAuthService;
import com.review.portal.service.impl.ManagerAuthServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller handling Manager Authentication (Phase 6.1).
 * Completely separate from Employee AuthController.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ManagerAuthController {

    private final ManagerAuthService managerAuthService;

    /**
     * Renders the Manager Login Page (Phase 6.1).
     */
    @GetMapping("/manager/login")
    public String showManagerLoginForm(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String unauthorized,
            HttpSession session,
            Model model) {

        if (managerAuthService.isAuthenticated(session)) {
            return "redirect:/manager/dashboard";
        }

        if (error != null) {
            model.addAttribute("errorMessage", "Invalid manager credentials. Please check email and password.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out of the Manager Workspace.");
        }
        if (unauthorized != null) {
            model.addAttribute("warningMessage", "Access denied. Please authenticate with manager credentials.");
        }

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("manager@gmail.com");
        model.addAttribute("loginRequest", loginRequest);

        return "manager-login";
    }

    /**
     * Processes Manager Login Submission (Phase 6.1).
     * On success, establishes session with managerId, managerName, managerEmail
     * and redirects to /manager/dashboard.
     */
    @PostMapping("/manager/login")
    public String processManagerLogin(
            @ModelAttribute LoginRequestDTO loginRequest,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Processing manager login submission for email: {}", loginRequest.getEmail());
        try {
            Manager manager = managerAuthService.authenticate(loginRequest, session);
            log.info("Manager authenticated successfully: {} (ID: {})", manager.getName(), manager.getId());
            redirectAttributes.addFlashAttribute("welcomeMessage", "Welcome to Manager Workspace, " + manager.getName() + "!");
            return "redirect:/manager/dashboard";
        } catch (Exception ex) {
            log.warn("Manager authentication failure for '{}': {}", loginRequest.getEmail(), ex.getMessage());
            model.addAttribute("errorMessage", "Invalid manager email or password");
            model.addAttribute("loginRequest", loginRequest);
            return "manager-login";
        }
    }

    /**
     * Terminates Manager Session (Phase 6.1).
     */
    @GetMapping("/manager/logout")
    public String processManagerLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        log.info("Processing manager logout");
        managerAuthService.logout(session);
        redirectAttributes.addFlashAttribute("logoutMessage", "You have been logged out of the Manager Workspace.");
        return "redirect:/manager/login?logout=true";
    }
}

