package com.review.portal.dto;

import com.review.portal.model.ReviewStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Review creation, updates, and responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;

    private Long managerId;

    private String managerName;

    @NotNull(message = "Technical score is required")
    @Min(value = 1, message = "Technical score must be at least 1")
    @Max(value = 5, message = "Technical score cannot exceed 5")
    private Integer technicalScore;

    @NotNull(message = "Teamwork score is required")
    @Min(value = 1, message = "Teamwork score must be at least 1")
    @Max(value = 5, message = "Teamwork score cannot exceed 5")
    private Integer teamworkScore;

    @NotNull(message = "Communication score is required")
    @Min(value = 1, message = "Communication score must be at least 1")
    @Max(value = 5, message = "Communication score cannot exceed 5")
    private Integer communicationScore;

    private Double overallRating;

    private ReviewStatus status;

    private String comment;

    private String feedback;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
