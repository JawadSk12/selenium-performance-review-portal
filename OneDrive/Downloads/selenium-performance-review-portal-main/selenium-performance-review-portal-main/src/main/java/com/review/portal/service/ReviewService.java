package com.review.portal.service;

import com.review.portal.dto.ReviewDTO;
import com.review.portal.model.Employee;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;

import java.util.List;

/**
 * Service interface for Performance Review domain operations and employee review lifecycle.
 */
public interface ReviewService {

    List<ReviewDTO> getAllReviews();

    ReviewDTO getReviewById(Long id);

    List<ReviewDTO> getReviewsByEmployee(Long employeeId);

    List<ReviewDTO> getReviewsByManager(Long managerId);

    List<ReviewDTO> getReviewsByStatus(ReviewStatus status);

    ReviewDTO createReview(ReviewDTO reviewDTO);

    ReviewDTO updateReview(Long id, ReviewDTO reviewDTO);

    ReviewDTO updateReviewStatus(Long id, ReviewStatus status, String managerComments);

    void deleteReview(Long id);

    /**
     * Submits an employee self-review with PENDING status and current date.
     */
    Review submitSelfReview(Review review, Employee employee);

    /**
     * Retrieves all reviews submitted by an employee ordered descending by date.
     */
    List<Review> getReviewsForEmployee(Employee employee);

    /**
     * Counts pending reviews for an employee.
     */
    long getPendingReviewCount(Employee employee);

    /**
     * Counts completed/approved reviews for an employee.
     */
    long getCompletedReviewCount(Employee employee);

    /**
     * Gets the latest review submitted by an employee.
     */
    Review getLatestReview(Employee employee);

    /**
     * Computes the dashboard status text ('No Reviews', 'Pending Review', 'Completed').
     */
    String getDashboardReviewStatus(Employee employee);
}
