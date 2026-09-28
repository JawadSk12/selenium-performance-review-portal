package com.review.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object holding Enterprise Manager Dashboard Key Performance Indicators (KPIs).
 * WEEK 6 — PHASE 6.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    private Long totalEmployees;
    private Long pendingReviews;
    private Long approvedReviews;
    private Double averageScore;

    /**
     * Formats the average score to 2 decimal places, displaying '0.00' if null.
     */
    public String getFormattedAverageScore() {
        if (averageScore == null || averageScore.isNaN()) {
            return "0.00";
        }
        return String.format("%.2f", averageScore);
    }

    /**
     * Compatibility alias for average score.
     */
    public Double getAveragePerformanceScore() {
        return averageScore;
    }

    public void setAveragePerformanceScore(Double averagePerformanceScore) {
        this.averageScore = averagePerformanceScore;
    }
}
