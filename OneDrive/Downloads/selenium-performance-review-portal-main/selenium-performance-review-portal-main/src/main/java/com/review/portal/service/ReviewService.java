package com.review.portal.service;

import com.review.portal.dto.ReviewDTO;
import com.review.portal.model.ReviewStatus;

import java.util.List;

/**
 * Service interface for Performance Review domain operations.
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
}
