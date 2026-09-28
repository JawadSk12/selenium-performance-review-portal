package com.review.portal.service;

import com.review.portal.dto.ReviewEvaluationDTO;
import com.review.portal.model.Review;
import jakarta.servlet.http.HttpSession;

/**
 * Service interface for Manager Review Evaluation Engine (Phase 6.3).
 */
public interface ManagerReviewService {

    /**
     * Retrieves a Review by its ID for the evaluation form.
     *
     * @param reviewId ID of the review to evaluate
     * @return Review entity
     */
    Review getReviewById(Long reviewId);

    /**
     * Processes the manager's evaluation:
     * - Calculates overall score = (technical + communication + teamwork + problemSolving) / 4
     * - Assigns grade based on score
     * - Sets manager_id from session
     * - Sets approved_date = LocalDate.now()
     * - Sets status = APPROVED or REJECTED
     * - Saves manager_comment to PostgreSQL
     *
     * @param reviewId        ID of the review being evaluated
     * @param evaluationDTO   Form data containing scores, comment, and action
     * @param session         HTTP session holding authenticated manager info
     */
    void evaluateReview(Long reviewId, ReviewEvaluationDTO evaluationDTO, HttpSession session);
}
