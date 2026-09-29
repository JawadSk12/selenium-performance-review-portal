package com.review.portal.service;

import com.review.portal.dto.DashboardStatsDTO;
import com.review.portal.model.Manager;
import com.review.portal.model.Review;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * Service interface for Enterprise Manager Dashboard operations (Phase 6.2).
 */
public interface DashboardService {

    /**
     * Calculates KPI metrics:
     * - Total Employees: COUNT(employee)
     * - Pending Reviews: COUNT(review WHERE status='PENDING')
     * - Approved Reviews: COUNT(review WHERE status='APPROVED')
     * - Average Score: ROUND(AVG(overall_score), 2) (0.00 if NULL)
     *
     * @return DashboardStatsDTO containing dynamic metrics
     */
    DashboardStatsDTO getDashboardStats();

    /**
     * Fetches all pending reviews awaiting manager evaluation.
     *
     * @return List of pending Review entities
     */
    List<Review> getPendingReviews();

    /**
     * Resolves the current authenticated Manager entity from the session.
     *
     * @param session Active HTTP session
     * @return Authenticated Manager entity
     */
    Manager getCurrentManager(HttpSession session);
}
