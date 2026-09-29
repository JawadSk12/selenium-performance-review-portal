package com.review.portal.controller;

import com.review.portal.dto.ApiResponse;
import com.review.portal.dto.EmployeeDTO;
import com.review.portal.model.Employee;
import com.review.portal.model.Review;
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

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller handling Employee Dashboard, Review History, and Employee REST operations.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ReviewService reviewService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    /**
     * Renders the Employee Dashboard (Phase 5.1 & 5.5).
     */
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Employee employee = employeeService.getCurrentEmployee(session);
        log.info("Rendering dashboard for employee: {} (ID: {})", employee.getName(), employee.getId());

        List<Review> reviews = reviewService.getReviewsForEmployee(employee);
        long pendingCount = reviewService.getPendingReviewCount(employee);
        long completedCount = reviewService.getCompletedReviewCount(employee);
        Review latestReview = reviewService.getLatestReview(employee);
        String reviewStatus = reviewService.getDashboardReviewStatus(employee);

        String lastReviewDate = (latestReview != null && latestReview.getReviewDate() != null)
                ? latestReview.getReviewDate().format(DATE_FORMATTER)
                : "No Reviews Yet";

        model.addAttribute("employee", employee);
        model.addAttribute("employeeName", employee.getName());
        model.addAttribute("department", employee.getDepartment() != null ? employee.getDepartment() : "IT");
        model.addAttribute("designation", employee.getDesignation() != null ? employee.getDesignation() : "Software Intern");
        model.addAttribute("email", employee.getEmail());
        model.addAttribute("reviewStatus", reviewStatus);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("lastReviewDate", lastReviewDate);
        model.addAttribute("reviews", reviews);

        return "dashboard";
    }

    /**
     * Renders the Review History Page (Phase 5.4).
     */
    @GetMapping("/employee/history")
    public String showReviewHistory(HttpSession session, Model model) {
        Employee employee = employeeService.getCurrentEmployee(session);
        log.info("Rendering review history for employee: {}", employee.getEmail());

        List<Review> reviews = reviewService.getReviewsForEmployee(employee);

        model.addAttribute("employee", employee);
        model.addAttribute("reviews", reviews);
        model.addAttribute("totalReviews", reviews.size());
        model.addAttribute("pendingCount", reviewService.getPendingReviewCount(employee));

        return "review-history";
    }

    // -------------------------------------------------------------
    // REST API Endpoints for Employee Management
    // -------------------------------------------------------------

    @ResponseBody
    @GetMapping("/api/employees")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees() {
        log.info("REST request to get all employees");
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.ok("Employees retrieved successfully", employees));
    }

    @ResponseBody
    @GetMapping("/api/employees/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long id) {
        log.info("REST request to get employee id: {}", id);
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.ok("Employee retrieved successfully", employee));
    }

    @ResponseBody
    @PostMapping("/api/employees")
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("REST request to create employee: {}", employeeDTO.getName());
        EmployeeDTO created = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(ApiResponse.ok("Employee created successfully", created), HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/api/employees/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("REST request to update employee id: {}", id);
        EmployeeDTO updated = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(ApiResponse.ok("Employee updated successfully", updated));
    }

    @ResponseBody
    @DeleteMapping("/api/employees/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        log.info("REST request to delete employee id: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.ok("Employee deleted successfully", null));
    }
}
