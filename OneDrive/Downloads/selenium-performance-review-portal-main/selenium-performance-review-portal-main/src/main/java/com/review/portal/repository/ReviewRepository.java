package com.review.portal.repository;

import com.review.portal.model.Employee;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Review entity management.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByEmployeeOrderByReviewDateDesc(Employee employee);

    List<Review> findByEmployee(Employee employee);

    List<Review> findByEmployeeId(Long employeeId);

    List<Review> findByManagerId(Long managerId);

    List<Review> findByStatus(ReviewStatus status);

    List<Review> findByEmployeeIdAndStatus(Long employeeId, ReviewStatus status);

    long countByEmployeeAndStatus(Employee employee, ReviewStatus status);

    long countByEmployee(Employee employee);
}
