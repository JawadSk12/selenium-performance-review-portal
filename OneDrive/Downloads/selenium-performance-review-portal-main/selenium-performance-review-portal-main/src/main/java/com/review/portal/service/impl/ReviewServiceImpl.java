package com.review.portal.service.impl;

import com.review.portal.dto.ReviewDTO;
import com.review.portal.model.ReviewStatus;
import com.review.portal.repository.EmployeeRepository;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.repository.ReviewRepository;
import com.review.portal.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Service implementation skeleton for Review operations.
 * Business logic to be finalized in Phase 2.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;

    @Override
    public List<ReviewDTO> getAllReviews() {
        log.info("Fetching all performance reviews");
        // Business logic to be implemented in Phase 2
        return Collections.emptyList();
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        log.info("Fetching review by id: {}", id);
        // Business logic to be implemented in Phase 2
        return null;
    }

    @Override
    public List<ReviewDTO> getReviewsByEmployee(Long employeeId) {
        log.info("Fetching reviews for employee id: {}", employeeId);
        // Business logic to be implemented in Phase 2
        return Collections.emptyList();
    }

    @Override
    public List<ReviewDTO> getReviewsByManager(Long managerId) {
        log.info("Fetching reviews for manager id: {}", managerId);
        // Business logic to be implemented in Phase 2
        return Collections.emptyList();
    }

    @Override
    public List<ReviewDTO> getReviewsByStatus(ReviewStatus status) {
        log.info("Fetching reviews by status: {}", status);
        // Business logic to be implemented in Phase 2
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        log.info("Creating review for employee id: {}", reviewDTO.getEmployeeId());
        // Business logic to be implemented in Phase 2
        return reviewDTO;
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        log.info("Updating review id: {}", id);
        // Business logic to be implemented in Phase 2
        return reviewDTO;
    }

    @Override
    @Transactional
    public ReviewDTO updateReviewStatus(Long id, ReviewStatus status, String managerComments) {
        log.info("Updating review id: {} status to: {}", id, status);
        // Business logic to be implemented in Phase 2
        return null;
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        log.info("Deleting review id: {}", id);
        // Business logic to be implemented in Phase 2
    }
}
