package com.review.portal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a Performance Review submitted for an employee and evaluated by a manager.
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"employee", "manager"})
@EqualsAndHashCode(exclude = {"employee", "manager"})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Min(value = 1, message = "Technical score must be at least 1")
    @Max(value = 5, message = "Technical score cannot exceed 5")
    @Column(name = "technical", nullable = false)
    private Integer technicalScore;

    @Min(value = 1, message = "Teamwork score must be at least 1")
    @Max(value = 5, message = "Teamwork score cannot exceed 5")
    @Column(name = "teamwork", nullable = false)
    private Integer teamworkScore;

    @Min(value = 1, message = "Communication score must be at least 1")
    @Max(value = 5, message = "Communication score cannot exceed 5")
    @Column(name = "communication", nullable = false)
    private Integer communicationScore;

    @Column(name = "overall_rating")
    private Double overallRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private ReviewStatus status = ReviewStatus.SUBMITTED;

    @Lob
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Lob
    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Helper method to compute overall rating average before persist/update if needed
     */
    @PrePersist
    @PreUpdate
    public void calculateOverallRating() {
        if (technicalScore != null && teamworkScore != null && communicationScore != null) {
            this.overallRating = Math.round(((technicalScore + teamworkScore + communicationScore) / 3.0) * 100.0) / 100.0;
        }
    }
}
