package com.review.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Manager Review Evaluation form submission (Phase 6.3).
 * Carries manager-assigned scores, comment, and approval decision.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEvaluationDTO {

    private Long reviewId;

    // Manager-assigned competency scores (1–10)
    private Integer technical;
    private Integer communication;
    private Integer teamwork;
    private Integer problemSolving;

    // Auto-calculated: (technical + communication + teamwork + problemSolving) / 4
    private Double overallScore;

    // Manager's textual comment
    private String managerComment;

    // Action submitted: "approve" or "reject"
    private String action;

    /**
     * Calculates the overall score as the average of the four competency scores.
     * Formula: (Technical + Communication + Teamwork + ProblemSolving) / 4
     */
    public Double calculateOverallScore() {
        int count = 0;
        double sum = 0.0;

        if (technical != null) { sum += technical; count++; }
        if (communication != null) { sum += communication; count++; }
        if (teamwork != null) { sum += teamwork; count++; }
        if (problemSolving != null) { sum += problemSolving; count++; }

        if (count == 0) return 0.0;
        return Math.round((sum / count) * 100.0) / 100.0;
    }

    /**
     * Generates a performance grade based on overall score.
     * 9–10:  Outstanding
     * 8–8.9: Excellent
     * 7–7.9: Good
     * Below 7: Needs Improvement
     */
    public String getGrade() {
        Double score = calculateOverallScore();
        if (score >= 9.0) return "Outstanding";
        if (score >= 8.0) return "Excellent";
        if (score >= 7.0) return "Good";
        return "Needs Improvement";
    }
}
