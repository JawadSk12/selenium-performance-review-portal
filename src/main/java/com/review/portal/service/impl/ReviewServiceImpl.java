package com.review.portal.service.impl;

import com.review.portal.dto.ReviewDTO;
import com.review.portal.exception.ResourceNotFoundException;
import com.review.portal.model.Employee;
import com.review.portal.model.Manager;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;
import com.review.portal.repository.EmployeeRepository;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.repository.ReviewRepository;
import com.review.portal.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ReviewService for employee self-review submissions,
 * review history queries, and review metrics.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public Review submitSelfReview(Review review, Employee employee) {
        log.info("Submitting self review for employee: {} (ID: {})", employee.getName(), employee.getId());
        review.setEmployee(employee);
        review.setStatus(ReviewStatus.PENDING);
        if (review.getReviewDate() == null) {
            review.setReviewDate(LocalDate.now());
        }
        review.calculateOverallScore();

        Review saved = reviewRepository.save(review);
        log.info("Review saved successfully with ID: {}, Status: {}, Date: {}",
                saved.getId(), saved.getStatus(), saved.getReviewDate());
        return saved;
    }

    @Override
    public List<Review> getReviewsForEmployee(Employee employee) {
        log.info("Fetching reviews for employee: {}", employee.getEmail());
        return reviewRepository.findByEmployeeOrderByReviewDateDesc(employee);
    }

    @Override
    public long getPendingReviewCount(Employee employee) {
        long count = reviewRepository.countByEmployeeAndStatus(employee, ReviewStatus.PENDING);
        if (count == 0) {
            count = reviewRepository.countByEmployeeAndStatus(employee, ReviewStatus.Pending);
        }
        return count;
    }

    @Override
    public long getCompletedReviewCount(Employee employee) {
        return reviewRepository.countByEmployeeAndStatus(employee, ReviewStatus.APPROVED);
    }

    @Override
    public Review getLatestReview(Employee employee) {
        List<Review> reviews = getReviewsForEmployee(employee);
        return reviews.isEmpty() ? null : reviews.get(0);
    }

    @Override
    public String getDashboardReviewStatus(Employee employee) {
        List<Review> reviews = getReviewsForEmployee(employee);
        if (reviews.isEmpty()) {
            return "No Reviews";
        }
        Review latest = reviews.get(0);
        if (latest.getStatus() == ReviewStatus.PENDING || latest.getStatus() == ReviewStatus.Pending) {
            return "Pending Review";
        }
        if (latest.getStatus() == ReviewStatus.APPROVED) {
            return "Completed";
        }
        return latest.getStatus().getDisplayName();
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        log.info("Fetching all performance reviews");
        return reviewRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        log.info("Fetching review by id: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        return mapToDTO(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByEmployee(Long employeeId) {
        log.info("Fetching reviews for employee id: {}", employeeId);
        return reviewRepository.findByEmployeeId(employeeId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByManager(Long managerId) {
        log.info("Fetching reviews for manager id: {}", managerId);
        return reviewRepository.findByManagerId(managerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByStatus(ReviewStatus status) {
        log.info("Fetching reviews by status: {}", status);
        return reviewRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        log.info("Creating review for employee id: {}", reviewDTO.getEmployeeId());
        Employee employee = employeeRepository.findById(reviewDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + reviewDTO.getEmployeeId()));

        Manager manager = null;
        if (reviewDTO.getManagerId() != null) {
            manager = managerRepository.findById(reviewDTO.getManagerId()).orElse(null);
        }

        Review review = Review.builder()
                .employee(employee)
                .manager(manager)
                .technical(reviewDTO.getTechnicalScore())
                .teamwork(reviewDTO.getTeamworkScore())
                .communication(reviewDTO.getCommunicationScore())
                .achievement(reviewDTO.getComment())
                .status(reviewDTO.getStatus() != null ? reviewDTO.getStatus() : ReviewStatus.PENDING)
                .reviewDate(LocalDate.now())
                .build();
        review.calculateOverallScore();

        Review saved = reviewRepository.save(review);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        log.info("Updating review id: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        review.setTechnical(reviewDTO.getTechnicalScore());
        review.setTeamwork(reviewDTO.getTeamworkScore());
        review.setCommunication(reviewDTO.getCommunicationScore());
        review.setAchievement(reviewDTO.getComment());
        review.calculateOverallScore();

        Review updated = reviewRepository.save(review);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public ReviewDTO updateReviewStatus(Long id, ReviewStatus status, String managerComments) {
        log.info("Updating review id: {} status to: {}", id, status);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        review.setStatus(status);
        if (managerComments != null) {
            review.setFutureGoal(managerComments);
        }

        Review updated = reviewRepository.save(review);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        log.info("Deleting review id: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        reviewRepository.delete(review);
    }

    private ReviewDTO mapToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .employeeId(review.getEmployee() != null ? review.getEmployee().getId() : null)
                .employeeName(review.getEmployee() != null ? review.getEmployee().getName() : null)
                .managerId(review.getManager() != null ? review.getManager().getId() : null)
                .managerName(review.getManager() != null ? review.getManager().getName() : null)
                .technicalScore(review.getTechnical())
                .teamworkScore(review.getTeamwork())
                .communicationScore(review.getCommunication())
                .overallRating(review.getOverallScore())
                .status(review.getStatus())
                .comment(review.getAchievement())
                .feedback(review.getFutureGoal())
                .build();
    }
}
