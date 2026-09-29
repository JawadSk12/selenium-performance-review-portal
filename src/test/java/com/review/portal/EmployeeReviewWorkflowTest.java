package com.review.portal;

import com.review.portal.model.Employee;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;
import com.review.portal.repository.EmployeeRepository;
import com.review.portal.repository.ReviewRepository;
import com.review.portal.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-End Integration Test for Phase 5 — Employee Review Workflow.
 * Verifies:
 * 5.1 Employee Dashboard rendering with employee profile & status
 * 5.2 Self-Review Form and POST submission
 * 5.3 PostgreSQL insertion with status = PENDING and reviewDate = current date
 * 5.4 Review History page and findByEmployeeOrderByReviewDateDesc repository query
 * 5.5 Automatic dashboard status transition from 'No Reviews' to 'Pending Review'
 */
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeReviewWorkflowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private Employee testEmployee;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        // Clear previous reviews to start from clean state
        reviewRepository.deleteAll();

        // Ensure primary demo employee exists
        testEmployee = employeeRepository.findByEmail("jawad@gmail.com")
                .orElseGet(() -> employeeRepository.save(
                        Employee.builder()
                                .name("Jawad Shaikh")
                                .email("jawad@gmail.com")
                                .password("1234")
                                .department("IT")
                                .designation("Software Intern")
                                .employeeCode("EMP-1001")
                                .build()
                ));

        // Create authenticated session
        session = new MockHttpSession();
        session.setAttribute(AuthServiceImpl.SESSION_EMPLOYEE_ID, testEmployee.getId());
        session.setAttribute(AuthServiceImpl.SESSION_EMPLOYEE_NAME, testEmployee.getName());
        session.setAttribute(AuthServiceImpl.SESSION_EMPLOYEE_EMAIL, testEmployee.getEmail());
        session.setAttribute(AuthServiceImpl.SESSION_EMPLOYEE_DEPT, testEmployee.getDepartment());
        session.setAttribute(AuthServiceImpl.SESSION_EMPLOYEE_ROLE, testEmployee.getDesignation());
    }

    @Test
    @DisplayName("5.1 & 5.5 Verify Initial Dashboard displays No Reviews")
    void testInitialDashboardState() throws Exception {
        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("employeeName", "Jawad Shaikh"))
                .andExpect(model().attribute("department", "IT"))
                .andExpect(model().attribute("designation", "Software Intern"))
                .andExpect(model().attribute("reviewStatus", "No Reviews"))
                .andExpect(model().attribute("pendingCount", 0L))
                .andExpect(content().string(containsString("Employee Dashboard")))
                .andExpect(content().string(containsString("Jawad Shaikh")))
                .andExpect(content().string(containsString("Submit Review")))
                .andExpect(content().string(containsString("View History")))
                .andExpect(content().string(containsString("Logout")));
    }

    @Test
    @DisplayName("5.2 Verify Self Review Form loads with all required fields")
    void testReviewFormRenders() throws Exception {
        mockMvc.perform(get("/review").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("review-form"))
                .andExpect(model().attributeExists("review"))
                .andExpect(content().string(containsString("Technical Proficiency")))
                .andExpect(content().string(containsString("Communication & Collaboration")))
                .andExpect(content().string(containsString("Teamwork & Cross-Functional Alignment")))
                .andExpect(content().string(containsString("Problem Solving & Critical Thinking")))
                .andExpect(content().string(containsString("Achievements & Contributions")))
                .andExpect(content().string(containsString("Future Goals & Professional Development")));
    }

    @Test
    @DisplayName("5.2, 5.3 & 5.5 Verify Review Submission, PostgreSQL Storage and Automatic Dashboard Update")
    void testReviewSubmissionAndDashboardAutoUpdate() throws Exception {
        // Step 1: Submit review via POST /review
        mockMvc.perform(post("/review")
                        .session(session)
                        .param("technical", "9")
                        .param("communication", "8")
                        .param("teamwork", "9")
                        .param("problemSolving", "8")
                        .param("achievement", "Delivered core performance portal and automated testing suite.")
                        .param("futureGoal", "Deepen Spring Security architecture and reactive programming."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(flash().attributeExists("successMessage"));

        // Step 2: Verify PostgreSQL entity state directly
        List<Review> reviewsInDb = reviewRepository.findByEmployeeOrderByReviewDateDesc(testEmployee);
        assertThat(reviewsInDb).hasSize(1);

        Review savedReview = reviewsInDb.get(0);
        assertThat(savedReview.getTechnical()).isEqualTo(9);
        assertThat(savedReview.getCommunication()).isEqualTo(8);
        assertThat(savedReview.getTeamwork()).isEqualTo(9);
        assertThat(savedReview.getProblemSolving()).isEqualTo(8);
        assertThat(savedReview.getAchievement()).isEqualTo("Delivered core performance portal and automated testing suite.");
        assertThat(savedReview.getStatus()).isEqualTo(ReviewStatus.PENDING);
        assertThat(savedReview.getReviewDate()).isEqualTo(LocalDate.now());

        // Step 3: Verify Dashboard automatically transitions from 'No Reviews' to 'Pending Review'
        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("reviewStatus", "Pending Review"))
                .andExpect(model().attribute("pendingCount", 1L))
                .andExpect(content().string(containsString("Pending Review")));
    }

    @Test
    @DisplayName("5.4 Verify Review History Page shows review records with ordered repository query")
    void testReviewHistoryPage() throws Exception {
        // Pre-create a review
        Review review = Review.builder()
                .employee(testEmployee)
                .technical(9)
                .communication(8)
                .teamwork(9)
                .problemSolving(8)
                .achievement("Successfully deployed phase 5.")
                .futureGoal("Expand test automation.")
                .status(ReviewStatus.PENDING)
                .reviewDate(LocalDate.now())
                .build();
        review.calculateOverallScore();
        reviewRepository.save(review);

        // Access GET /employee/history
        mockMvc.perform(get("/employee/history").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("review-history"))
                .andExpect(model().attribute("totalReviews", 1))
                .andExpect(model().attribute("pendingCount", 1L))
                .andExpect(content().string(containsString("Review History")))
                .andExpect(content().string(containsString("Pending")))
                .andExpect(content().string(containsString("--"))) // Pending overall score shows '--'
                .andExpect(content().string(containsString("View Details")));
    }
}
