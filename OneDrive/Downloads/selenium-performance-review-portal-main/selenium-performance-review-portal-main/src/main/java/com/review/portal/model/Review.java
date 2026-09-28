package com.review.portal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;

/**
 * Entity representing an Employee Performance Review.
 * Maps to PostgreSQL 'review' table.
 */
@Entity
@Table(name = "review")
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Min(value = 1, message = "Technical score must be at least 1")
    @Max(value = 10, message = "Technical score cannot exceed 10")
    @Column(name = "technical")
    private Integer technical;

    @Min(value = 1, message = "Communication score must be at least 1")
    @Max(value = 10, message = "Communication score cannot exceed 10")
    @Column(name = "communication")
    private Integer communication;

    @Min(value = 1, message = "Teamwork score must be at least 1")
    @Max(value = 10, message = "Teamwork score cannot exceed 10")
    @Column(name = "teamwork")
    private Integer teamwork;

    @Min(value = 1, message = "Problem solving score must be at least 1")
    @Max(value = 10, message = "Problem solving score cannot exceed 10")
    @Column(name = "problem_solving")
    private Integer problemSolving;

    @Column(name = "achievement", columnDefinition = "TEXT")
    private String achievement;

    @Column(name = "future_goal", columnDefinition = "TEXT")
    private String futureGoal;

    @Column(name = "overall_score")
    private Double overallScore;

    @Convert(converter = ReviewStatusConverter.class)
    @Column(name = "status", length = 20)
    @Builder.Default
    private ReviewStatus status = ReviewStatus.PENDING;

    @Column(name = "review_date")
    @Builder.Default
    private LocalDate reviewDate = LocalDate.now();

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (this.reviewDate == null) {
            this.reviewDate = LocalDate.now();
        }
        if (this.status == null) {
            this.status = ReviewStatus.PENDING;
        }
        calculateOverallScore();
    }

    public void calculateOverallScore() {
        int count = 0;
        double sum = 0.0;
        if (technical != null) {
            sum += technical;
            count++;
        }
        if (communication != null) {
            sum += communication;
            count++;
        }
        if (teamwork != null) {
            sum += teamwork;
            count++;
        }
        if (problemSolving != null) {
            sum += problemSolving;
            count++;
        }

        if (count > 0) {
            this.overallScore = Math.round((sum / count) * 100.0) / 100.0;
        }
    }

    // Compatibility and convenience getters/setters
    public Integer getTechnicalScore() {
        return technical;
    }

    public void setTechnicalScore(Integer technicalScore) {
        this.technical = technicalScore;
    }

    public Integer getCommunicationScore() {
        return communication;
    }

    public void setCommunicationScore(Integer communicationScore) {
        this.communication = communicationScore;
    }

    public Integer getTeamworkScore() {
        return teamwork;
    }

    public void setTeamworkScore(Integer teamworkScore) {
        this.teamwork = teamworkScore;
    }

    public String getAchievements() {
        return achievement;
    }

    public void setAchievements(String achievements) {
        this.achievement = achievements;
    }

    public String getFutureGoals() {
        return futureGoal;
    }

    public void setFutureGoals(String futureGoals) {
        this.futureGoal = futureGoals;
    }

    public Double getOverallRating() {
        return overallScore;
    }

    public void setOverallRating(Double overallRating) {
        this.overallScore = overallRating;
    }
}
