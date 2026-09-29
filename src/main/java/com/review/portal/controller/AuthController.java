package com.review.portal.controller;

import com.review.portal.dto.LoginRequestDTO;
import com.review.portal.model.Employee;
import com.review.portal.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller handling Employee Authentication (Login, Logout, Session lifecycle).
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/")
    public String rootRedirect(HttpSession session) {
        if (authService.isAuthenticated(session)) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String unauthorized,
            HttpSession session,
            Model model) {

        if (authService.isAuthenticated(session)) {
            return "redirect:/dashboard";
        }

        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully.");
        }
        if (unauthorized != null) {
            model.addAttribute("warningMessage", "Please log in to access the requested portal resource.");
        }

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("jawad@gmail.com"); // Prepopulate demo credentials for seamless testing
        model.addAttribute("loginRequest", loginRequest);

        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @ModelAttribute LoginRequestDTO loginRequest,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Processing login submission for email: {}", loginRequest.getEmail());
        try {
            Employee employee = authService.authenticate(loginRequest, session);
            log.info("Employee authenticated: {}", employee.getEmail());
            redirectAttributes.addFlashAttribute("welcomeMessage", "Welcome back, " + employee.getName() + "!");
            return "redirect:/dashboard";
        } catch (Exception ex) {
            log.warn("Authentication failed for '{}': {}", loginRequest.getEmail(), ex.getMessage());
            model.addAttribute("errorMessage", "Invalid email or password");
            model.addAttribute("loginRequest", loginRequest);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String processLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        log.info("Processing logout request");
        authService.logout(session);
        redirectAttributes.addFlashAttribute("logoutMessage", "You have been logged out successfully.");
        return "redirect:/login?logout=true";
    }
}
