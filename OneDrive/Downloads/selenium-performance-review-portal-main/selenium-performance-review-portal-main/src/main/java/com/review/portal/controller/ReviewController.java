package com.review.portal.controller;

import com.review.portal.dto.ApiResponse;
import com.review.portal.dto.ReviewDTO;
import com.review.portal.model.Employee;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;
import com.review.portal.service.EmployeeService;
import com.review.portal.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller handling Self-Review Submission (Phase 5.2, 5.3),
 * Review Form rendering, and Review REST endpoints.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final EmployeeService employeeService;

    /**
     * Renders the Self Review Form (Phase 5.2).
     */
    @GetMapping({"/review", "/employee/review"})
    public String showReviewForm(HttpSession session, Model model) {
        Employee employee = employeeService.getCurrentEmployee(session);
        log.info("Rendering review form for employee: {}", employee.getEmail());

        Review review = Review.builder()
                .technical(9)
                .communication(8)
                .teamwork(9)
                .problemSolving(8)
                .status(ReviewStatus.PENDING)
                .reviewDate(LocalDate.now())
                .build();

        model.addAttribute("review", review);
        model.addAttribute("employee", employee);
        return "review-form";
    }

    /**
     * Handles Self Review Submission (Phase 5.2 & 5.3).
     * Saves review directly into PostgreSQL with status = PENDING and reviewDate = current date.
     */
    @PostMapping({"/review", "/employee/review"})
    public String submitReview(
            @ModelAttribute Review review,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Employee employee = employeeService.getCurrentEmployee(session);
        log.info("Processing self-review submission for employee: {} (ID: {})", employee.getName(), employee.getId());

        Review savedReview = reviewService.submitSelfReview(review, employee);

        redirectAttributes.addFlashAttribute("successMessage",
                "Your performance review has been submitted successfully! Status: " + savedReview.getStatus());

        return "redirect:/dashboard";
    }

    // -------------------------------------------------------------
    // REST API Endpoints for Performance Review Management
    // -------------------------------------------------------------

    @ResponseBody
    @GetMapping("/api/reviews")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getAllReviews() {
        log.info("REST request to get all reviews");
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(ApiResponse.ok("Reviews retrieved successfully", reviews));
    }

    @ResponseBody
    @GetMapping("/api/reviews/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> getReviewById(@PathVariable Long id) {
        log.info("REST request to get review id: {}", id);
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(ApiResponse.ok("Review retrieved successfully", review));
    }

    @ResponseBody
    @GetMapping("/api/reviews/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByEmployee(@PathVariable Long employeeId) {
        log.info("REST request to get reviews for employee id: {}", employeeId);
        List<ReviewDTO> reviews = reviewService.getReviewsByEmployee(employeeId);
        return ResponseEntity.ok(ApiResponse.ok("Employee reviews retrieved successfully", reviews));
    }

    @ResponseBody
    @GetMapping("/api/reviews/manager/{managerId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByManager(@PathVariable Long managerId) {
        log.info("REST request to get reviews for manager id: {}", managerId);
        List<ReviewDTO> reviews = reviewService.getReviewsByManager(managerId);
        return ResponseEntity.ok(ApiResponse.ok("Manager reviews retrieved successfully", reviews));
    }

    @ResponseBody
    @GetMapping("/api/reviews/status/{status}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByStatus(@PathVariable ReviewStatus status) {
        log.info("REST request to get reviews by status: {}", status);
        List<ReviewDTO> reviews = reviewService.getReviewsByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok("Reviews filtered by status", reviews));
    }

    @ResponseBody
    @PostMapping("/api/reviews")
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        log.info("REST request to create review for employee: {}", reviewDTO.getEmployeeId());
        ReviewDTO created = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(ApiResponse.ok("Review created successfully", created), HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/api/reviews/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        log.info("REST request to update review id: {}", id);
        ReviewDTO updated = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(ApiResponse.ok("Review updated successfully", updated));
    }

    @ResponseBody
    @PatchMapping("/api/reviews/{id}/status")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReviewStatus(
            @PathVariable Long id,
            @RequestParam ReviewStatus status,
            @RequestParam(required = false) String comments) {
        log.info("REST request to update review id: {} status to: {}", id, status);
        ReviewDTO updated = reviewService.updateReviewStatus(id, status, comments);
        return ResponseEntity.ok(ApiResponse.ok("Review status updated successfully", updated));
    }

    @ResponseBody
    @DeleteMapping("/api/reviews/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        log.info("REST request to delete review id: {}", id);
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.ok("Review deleted successfully", null));
    }
}
