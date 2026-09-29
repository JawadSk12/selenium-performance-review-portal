package com.review.portal.controller;

import com.review.portal.dto.DashboardStatsDTO;
import com.review.portal.model.Manager;
import com.review.portal.model.Review;
import com.review.portal.service.DashboardService;
import com.review.portal.service.impl.ManagerAuthServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller handling Enterprise Manager Dashboard (Phase 6.2).
 * Completely replaces the "Manager Workspace" placeholder with the full enterprise dashboard.
 */
@Slf4j
@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerDashboardController {

    private final DashboardService dashboardService;

    /**
     * Renders the Enterprise HR Manager Dashboard.
     * Route: /manager/dashboard
     * Validates manager session and sends: managerName, managerEmail, stats, pendingReviews.
     */
    @GetMapping("/dashboard")
    public String showManagerDashboard(HttpSession session, Model model) {
        // Check manager session
        if (session == null || session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_ID) == null) {
            log.warn("Unauthenticated access attempt to /manager/dashboard. Redirecting to login.");
            return "redirect:/manager/login?unauthorized=true";
        }

        Manager currentManager = dashboardService.getCurrentManager(session);

        String managerName = (String) session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_NAME);
        if (managerName == null && currentManager != null) {
            managerName = currentManager.getName();
        }

        String managerEmail = (String) session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_EMAIL);
        if (managerEmail == null && currentManager != null) {
            managerEmail = currentManager.getEmail();
        }

        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        List<Review> pendingReviews = dashboardService.getPendingReviews();

        log.info("Rendering Enterprise HR Manager Dashboard for: {} ({}) with {} pending reviews",
                managerName, managerEmail, pendingReviews.size());

        model.addAttribute("managerName", managerName);
        model.addAttribute("managerEmail", managerEmail);
        model.addAttribute("stats", stats);
        model.addAttribute("pendingReviews", pendingReviews);

        return "manager-dashboard";
    }
}
