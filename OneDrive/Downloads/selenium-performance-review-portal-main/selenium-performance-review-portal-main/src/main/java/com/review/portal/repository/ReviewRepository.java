package com.review.portal.repository;

import com.review.portal.model.Employee;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Review entity management, evaluation engine and dashboard queries.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByEmployeeOrderByReviewDateDesc(Employee employee);

    List<Review> findByEmployee(Employee employee);

    List<Review> findByEmployeeId(Long employeeId);

    List<Review> findByManagerId(Long managerId);

    List<Review> findByStatus(ReviewStatus status);

    List<Review> findByStatusOrderByReviewDateDesc(ReviewStatus status);

    @Query("SELECT r FROM Review r WHERE r.status IN :statuses ORDER BY r.reviewDate DESC")
    List<Review> findByStatusInOrderByReviewDateDesc(@Param("statuses") List<ReviewStatus> statuses);

    List<Review> findByEmployeeIdAndStatus(Long employeeId, ReviewStatus status);

    long countByEmployeeAndStatus(Employee employee, ReviewStatus status);

    long countByEmployee(Employee employee);

    long countByStatus(ReviewStatus status);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.status IN :statuses")
    long countByStatusIn(@Param("statuses") List<ReviewStatus> statuses);

    @Query("SELECT AVG(r.overallScore) FROM Review r WHERE r.overallScore IS NOT NULL")
    Double getAveragePerformanceScore();

    @Query("SELECT COALESCE(ROUND(AVG(r.overallScore), 2), 0.0) FROM Review r WHERE r.overallScore IS NOT NULL")
    Double getAverageScore();
}
