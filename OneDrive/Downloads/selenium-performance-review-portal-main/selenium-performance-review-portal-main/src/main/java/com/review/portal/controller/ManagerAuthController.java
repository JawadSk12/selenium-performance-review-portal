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

    /**
     * Temporary placeholder for /manager/dashboard until Phase 6.2 dashboard is built.
     */
    @GetMapping("/manager/dashboard")
    @ResponseBody
    public String managerDashboardPlaceholder(HttpSession session) {
        String managerName = (String) session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_NAME);
        String managerEmail = (String) session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_EMAIL);
        Long managerId = (Long) session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_ID);

        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Manager Dashboard - Workspace</title>" +
                "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>" +
                "<style>body{background:#090d16;color:#f1f5f9;font-family:system-ui,-apple-system,sans-serif;min-height:100vh;display:flex;align-items:center;justify-content:center;}</style>" +
                "</head><body><div class='card bg-dark border-secondary p-4 shadow-lg text-center' style='max-width:500px;'>" +
                "<h3 class='text-primary mb-2'>Manager Workspace</h3>" +
                "<p class='text-muted small mb-3'>Phase 6.1 Authentication Active &bull; Dashboard Coming in Phase 6.2</p>" +
                "<div class='alert alert-secondary text-start small mb-3'>" +
                "<div><strong>Manager Name:</strong> " + (managerName != null ? managerName : "N/A") + "</div>" +
                "<div><strong>Manager Email:</strong> " + (managerEmail != null ? managerEmail : "N/A") + "</div>" +
                "<div><strong>Manager ID:</strong> " + (managerId != null ? managerId : "N/A") + "</div>" +
                "</div>" +
                "<a href='/manager/logout' class='btn btn-outline-danger btn-sm'>Logout</a>" +
                "</div></body></html>";
    }
}
