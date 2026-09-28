package com.review.portal.controller;

import com.review.portal.dto.ReviewEvaluationDTO;
import com.review.portal.model.Review;
import com.review.portal.service.ManagerReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller handling Manager Review Evaluation Engine (Phase 6.3).
 * Serves the evaluation form and processes approve/reject decisions.
 */
@Slf4j
@Controller
@RequestMapping("/manager/review")
@RequiredArgsConstructor
public class ManagerReviewController {

    // Session attribute keys (match ManagerAuthServiceImpl constants on feature-manager-auth branch)
    private static final String SESSION_MANAGER_ID    = "managerId";
    private static final String SESSION_MANAGER_NAME  = "managerName";
    private static final String SESSION_MANAGER_EMAIL = "managerEmail";

    private final ManagerReviewService managerReviewService;

    /**
     * GET /manager/review/{reviewId}
     * Opens the review evaluation form with employee's self-review pre-loaded.
     */
    @GetMapping("/{reviewId}")
    public String showReviewEvaluationForm(
            @PathVariable Long reviewId,
            HttpSession session,
            Model model) {

        // Guard: only authenticated managers may access
        if (session == null || session.getAttribute(SESSION_MANAGER_ID) == null) {
            log.warn("Unauthenticated access attempt to /manager/review/{}. Redirecting.", reviewId);
            return "redirect:/manager/login?unauthorized=true";
        }

        Review review = managerReviewService.getReviewById(reviewId);
        log.info("Opening evaluation form for review ID: {} (Employee: {})",
                reviewId, review.getEmployee() != null ? review.getEmployee().getName() : "Unknown");

        // Pre-populate DTO with existing scores from employee's self-review
        ReviewEvaluationDTO evaluationDTO = ReviewEvaluationDTO.builder()
                .reviewId(reviewId)
                .technical(review.getTechnical())
                .communication(review.getCommunication())
                .teamwork(review.getTeamwork())
                .problemSolving(review.getProblemSolving())
                .managerComment(review.getManagerComment())
                .build();

        model.addAttribute("review", review);
        model.addAttribute("evaluationDTO", evaluationDTO);
        model.addAttribute("managerName", session.getAttribute(SESSION_MANAGER_NAME));
        model.addAttribute("managerEmail", session.getAttribute(SESSION_MANAGER_EMAIL));

        return "manager-review";
    }

    /**
     * POST /manager/review/{reviewId}/evaluate
     * Processes manager's evaluation (Approve or Reject).
     * Saves: overall_score, manager_comment, manager_id, approved_date, status to PostgreSQL.
     * Redirects back to /manager/dashboard.
     */
    @PostMapping("/{reviewId}/evaluate")
    public String processEvaluation(
            @PathVariable Long reviewId,
            @ModelAttribute ReviewEvaluationDTO evaluationDTO,
            @RequestParam(name = "action") String action,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Guard: only authenticated managers
        if (session == null || session.getAttribute(SESSION_MANAGER_ID) == null) {
            return "redirect:/manager/login?unauthorized=true";
        }

        evaluationDTO.setReviewId(reviewId);
        evaluationDTO.setAction(action);

        log.info("Processing evaluation for review ID: {} | Action: {} | Scores T:{} C:{} TW:{} PS:{}",
                reviewId, action,
                evaluationDTO.getTechnical(), evaluationDTO.getCommunication(),
                evaluationDTO.getTeamwork(), evaluationDTO.getProblemSolving());

        try {
            managerReviewService.evaluateReview(reviewId, evaluationDTO, session);

            String grade = evaluationDTO.getGrade();
            Double score = evaluationDTO.calculateOverallScore();

            if ("approve".equalsIgnoreCase(action)) {
                redirectAttributes.addFlashAttribute("successMessage",
                        String.format("Review approved successfully. Overall Score: %.2f | Grade: %s", score, grade));
            } else {
                redirectAttributes.addFlashAttribute("warningMessage",
                        "Review has been rejected and returned to employee.");
            }
        } catch (Exception ex) {
            log.error("Error evaluating review ID {}: {}", reviewId, ex.getMessage(), ex);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to process evaluation: " + ex.getMessage());
        }

        return "redirect:/manager/dashboard";
    }
}
