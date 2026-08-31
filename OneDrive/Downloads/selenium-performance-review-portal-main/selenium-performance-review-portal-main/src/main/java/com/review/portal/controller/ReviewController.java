package com.review.portal.controller;

import com.review.portal.dto.ApiResponse;
import com.review.portal.dto.ReviewDTO;
import com.review.portal.model.ReviewStatus;
import com.review.portal.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller handling Performance Review management endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getAllReviews() {
        log.info("REST request to get all reviews");
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(ApiResponse.ok("Reviews retrieved successfully", reviews));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> getReviewById(@PathVariable Long id) {
        log.info("REST request to get review id: {}", id);
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(ApiResponse.ok("Review retrieved successfully", review));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByEmployee(@PathVariable Long employeeId) {
        log.info("REST request to get reviews for employee id: {}", employeeId);
        List<ReviewDTO> reviews = reviewService.getReviewsByEmployee(employeeId);
        return ResponseEntity.ok(ApiResponse.ok("Employee reviews retrieved successfully", reviews));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByManager(@PathVariable Long managerId) {
        log.info("REST request to get reviews for manager id: {}", managerId);
        List<ReviewDTO> reviews = reviewService.getReviewsByManager(managerId);
        return ResponseEntity.ok(ApiResponse.ok("Manager reviews retrieved successfully", reviews));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByStatus(@PathVariable ReviewStatus status) {
        log.info("REST request to get reviews by status: {}", status);
        List<ReviewDTO> reviews = reviewService.getReviewsByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok("Reviews filtered by status", reviews));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        log.info("REST request to create review for employee: {}", reviewDTO.getEmployeeId());
        ReviewDTO created = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(ApiResponse.ok("Review created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        log.info("REST request to update review id: {}", id);
        ReviewDTO updated = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(ApiResponse.ok("Review updated successfully", updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReviewStatus(
            @PathVariable Long id,
            @RequestParam ReviewStatus status,
            @RequestParam(required = false) String comments) {
        log.info("REST request to update review id: {} status to: {}", id, status);
        ReviewDTO updated = reviewService.updateReviewStatus(id, status, comments);
        return ResponseEntity.ok(ApiResponse.ok("Review status updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        log.info("REST request to delete review id: {}", id);
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.ok("Review deleted successfully", null));
    }
}
