package com.review.portal.service.impl;

import com.review.portal.dto.ReviewEvaluationDTO;
import com.review.portal.model.Manager;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.repository.ReviewRepository;
import com.review.portal.service.ManagerReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Implementation of Manager Review Evaluation Engine (Phase 6.3).
 * Processes manager evaluation, calculates overall score, assigns grade,
 * and persists: overall_score, manager_comment, manager_id, approved_date, status.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerReviewServiceImpl implements ManagerReviewService {

    // Session attribute keys (match ManagerAuthServiceImpl constants on feature-manager-auth branch)
    private static final String SESSION_MANAGER_ID = "managerId";

    private final ReviewRepository reviewRepository;
    private final ManagerRepository managerRepository;

    @Override
    @Transactional(readOnly = true)
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));
    }

    @Override
    @Transactional
    public void evaluateReview(Long reviewId, ReviewEvaluationDTO dto, HttpSession session) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        // 1. Calculate overall score: (technical + communication + teamwork + problemSolving) / 4
        Double overallScore = dto.calculateOverallScore();
        review.setOverallScore(overallScore);

        log.info("Evaluating review ID: {} | Scores T:{} C:{} TW:{} PS:{} | Overall: {} | Grade: {}",
                reviewId,
                dto.getTechnical(), dto.getCommunication(),
                dto.getTeamwork(), dto.getProblemSolving(),
                overallScore, dto.getGrade());

        // 2. Set manager-assigned competency scores
        review.setTechnical(dto.getTechnical());
        review.setCommunication(dto.getCommunication());
        review.setTeamwork(dto.getTeamwork());
        review.setProblemSolving(dto.getProblemSolving());

        // 3. Set manager comment
        review.setManagerComment(dto.getManagerComment());

        // 4. Resolve manager from session and set manager_id
        Object mgrIdObj = session.getAttribute(SESSION_MANAGER_ID);
        if (mgrIdObj != null) {
            Long mgrId = (mgrIdObj instanceof Long) ? (Long) mgrIdObj
                    : Long.valueOf(mgrIdObj.toString());
            managerRepository.findById(mgrId).ifPresent(review::setManager);
        } else {
            // Fallback: look up default manager
            managerRepository.findByEmail("manager@gmail.com")
                    .ifPresent(review::setManager);
        }

        // 5. Set status (APPROVED or REJECTED) and approved_date
        String action = dto.getAction();
        if ("approve".equalsIgnoreCase(action)) {
            review.setStatus(ReviewStatus.APPROVED);
            review.setApprovedDate(LocalDate.now());
            log.info("Review ID {} APPROVED by manager", reviewId);
        } else if ("reject".equalsIgnoreCase(action)) {
            review.setStatus(ReviewStatus.REJECTED);
            review.setApprovedDate(LocalDate.now());
            log.info("Review ID {} REJECTED by manager", reviewId);
        } else {
            review.setStatus(ReviewStatus.UNDER_REVIEW);
            log.warn("Unknown action '{}' for review ID {}. Marking as UNDER_REVIEW.", action, reviewId);
        }

        // 6. Persist all fields to PostgreSQL
        reviewRepository.save(review);
        log.info("Review ID {} evaluation saved to PostgreSQL. Status: {}, Score: {}",
                reviewId, review.getStatus(), review.getOverallScore());
    }
}
