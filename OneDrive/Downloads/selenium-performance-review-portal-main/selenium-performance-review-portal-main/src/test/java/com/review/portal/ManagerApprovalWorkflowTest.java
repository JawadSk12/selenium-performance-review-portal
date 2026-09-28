package com.review.portal;

import com.review.portal.model.Employee;
import com.review.portal.model.Manager;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;
import com.review.portal.repository.EmployeeRepository;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-End Workflow & Integration Test for Phase 6 — Manager Review & Evaluation Engine.
 * Verifies:
 * 6.1 Manager Authentication, Session Management, and Route Protection
 * 6.2 Enterprise Manager Dashboard rendering, KPI cards, and pending reviews table
 * 6.3 Manager Review Evaluation Engine:
 *     - Opening review evaluation form preloaded with employee self-review
 *     - Manager score evaluation, auto-calculated overall score, and grade
 *     - Approval workflow (status = APPROVED, approved_date, manager_comment, manager_id)
 *     - Rejection workflow (status = REJECTED, manager_comment)
 *     - Unauthenticated access redirection to /manager/login
 */
@SpringBootTest
@AutoConfigureMockMvc
class ManagerApprovalWorkflowTest {

    private static final String SESSION_MANAGER_ID    = "managerId";
    private static final String SESSION_MANAGER_NAME  = "managerName";
    private static final String SESSION_MANAGER_EMAIL = "managerEmail";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private Manager testManager;
    private Employee testEmployee;
    private Review pendingReview;
    private MockHttpSession managerSession;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();

        // 1. Ensure test manager exists
        testManager = managerRepository.findByEmail("manager@gmail.com")
                .orElseGet(() -> managerRepository.save(
                        Manager.builder()
                                .name("Sarah Jenkins")
                                .email("manager@gmail.com")
                                .password("1234")
                                .department("Engineering")
                                .build()
                ));

        // 2. Ensure test employee exists
        testEmployee = employeeRepository.findByEmail("jawad@gmail.com")
                .orElseGet(() -> employeeRepository.save(
                        Employee.builder()
                                .name("Jawad Shaikh")
                                .email("jawad@gmail.com")
                                .password("1234")
                                .department("IT")
                                .designation("Software Intern")
                                .employeeCode("EMP-1001")
                                .manager(testManager)
                                .build()
                ));

        // 3. Create a pending self-review submitted by employee
        pendingReview = Review.builder()
                .employee(testEmployee)
                .technical(8)
                .communication(8)
                .teamwork(8)
                .problemSolving(8)
                .achievement("Built core microservices and automated testing framework.")
                .futureGoal("Master distributed systems and Kafka streaming.")
                .status(ReviewStatus.PENDING)
                .reviewDate(LocalDate.now())
                .build();
        pendingReview.calculateOverallScore();
        pendingReview = reviewRepository.save(pendingReview);

        // 4. Authenticated Manager HTTP Session
        managerSession = new MockHttpSession();
        managerSession.setAttribute(SESSION_MANAGER_ID, testManager.getId());
        managerSession.setAttribute(SESSION_MANAGER_NAME, testManager.getName());
        managerSession.setAttribute(SESSION_MANAGER_EMAIL, testManager.getEmail());
    }

    @Test
    @DisplayName("6.1 Verify Manager Login Page loads successfully")
    void testManagerLoginPageLoads() throws Exception {
        mockMvc.perform(get("/manager/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-login"))
                .andExpect(content().string(containsString("Manager Login")));
    }

    @Test
    @DisplayName("6.1 Verify Route Protection redirects unauthenticated users to /manager/login")
    void testRouteProtectionUnauthenticated() throws Exception {
        // Without session, accessing dashboard must redirect
        mockMvc.perform(get("/manager/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/login?unauthorized=true"));

        // Without session, accessing evaluation form must redirect
        mockMvc.perform(get("/manager/review/" + pendingReview.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/login?unauthorized=true"));
    }

    @Test
    @DisplayName("6.2 Verify Enterprise Manager Dashboard renders with KPI stats and Pending Table")
    void testManagerDashboardRenders() throws Exception {
        mockMvc.perform(get("/manager/dashboard").session(managerSession))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-dashboard"))
                .andExpect(model().attributeExists("stats"))
                .andExpect(model().attributeExists("pendingReviews"))
                .andExpect(model().attribute("managerName", testManager.getName()))
                .andExpect(content().string(containsString("Performance Review Management")))
                .andExpect(content().string(containsString("Jawad Shaikh")))
                .andExpect(content().string(containsString("Review")));
    }

    @Test
    @DisplayName("6.3 Verify Evaluation Form loads pre-populated with Employee Self-Review data")
    void testEvaluationFormRenders() throws Exception {
        mockMvc.perform(get("/manager/review/" + pendingReview.getId()).session(managerSession))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-review"))
                .andExpect(model().attributeExists("review"))
                .andExpect(model().attributeExists("evaluationDTO"))
                .andExpect(content().string(containsString("Jawad Shaikh")))
                .andExpect(content().string(containsString("Built core microservices and automated testing framework.")))
                .andExpect(content().string(containsString("Master distributed systems and Kafka streaming.")))
                .andExpect(content().string(containsString("Approve")))
                .andExpect(content().string(containsString("Reject")));
    }

    @Test
    @DisplayName("6.3 Verify Manager Review Approval Workflow, Score Calculation & PostgreSQL Persistence")
    void testManagerReviewApprovalWorkflow() throws Exception {
        // Manager evaluates: sets scores (9, 9, 8, 10 -> overall 9.0), manager comment, action="approve"
        mockMvc.perform(post("/manager/review/" + pendingReview.getId() + "/evaluate")
                        .session(managerSession)
                        .param("technical", "9")
                        .param("communication", "9")
                        .param("teamwork", "8")
                        .param("problemSolving", "10")
                        .param("managerComment", "Outstanding technical leadership and high delivery velocity.")
                        .param("action", "approve"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/dashboard"))
                .andExpect(flash().attributeExists("successMessage"));

        // Verify entity state updated in PostgreSQL
        Optional<Review> updatedOpt = reviewRepository.findById(pendingReview.getId());
        assertThat(updatedOpt).isPresent();

        Review updated = updatedOpt.get();
        assertThat(updated.getStatus()).isEqualTo(ReviewStatus.APPROVED);
        assertThat(updated.getOverallScore()).isEqualTo(9.0);
        assertThat(updated.getTechnical()).isEqualTo(9);
        assertThat(updated.getCommunication()).isEqualTo(9);
        assertThat(updated.getTeamwork()).isEqualTo(8);
        assertThat(updated.getProblemSolving()).isEqualTo(10);
        assertThat(updated.getManagerComment()).isEqualTo("Outstanding technical leadership and high delivery velocity.");
        assertThat(updated.getApprovedDate()).isEqualTo(LocalDate.now());
        assertThat(updated.getManager()).isNotNull();
        assertThat(updated.getManager().getId()).isEqualTo(testManager.getId());
    }

    @Test
    @DisplayName("6.3 Verify Manager Review Rejection Workflow & PostgreSQL Persistence")
    void testManagerReviewRejectionWorkflow() throws Exception {
        // Manager evaluates: sets action="reject" with feedback
        mockMvc.perform(post("/manager/review/" + pendingReview.getId() + "/evaluate")
                        .session(managerSession)
                        .param("technical", "5")
                        .param("communication", "5")
                        .param("teamwork", "6")
                        .param("problemSolving", "5")
                        .param("managerComment", "Objectives were not met for this cycle. Needs improvement.")
                        .param("action", "reject"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/dashboard"))
                .andExpect(flash().attributeExists("warningMessage"));

        // Verify entity state updated in PostgreSQL
        Optional<Review> updatedOpt = reviewRepository.findById(pendingReview.getId());
        assertThat(updatedOpt).isPresent();

        Review updated = updatedOpt.get();
        assertThat(updated.getStatus()).isEqualTo(ReviewStatus.REJECTED);
        assertThat(updated.getOverallScore()).isEqualTo(5.25);
        assertThat(updated.getManagerComment()).isEqualTo("Objectives were not met for this cycle. Needs improvement.");
        assertThat(updated.getApprovedDate()).isEqualTo(LocalDate.now());
    }
}
