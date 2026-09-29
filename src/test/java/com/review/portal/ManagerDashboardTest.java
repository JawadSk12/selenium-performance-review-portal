package com.review.portal;

import com.review.portal.dto.DashboardStatsDTO;
import com.review.portal.model.Employee;
import com.review.portal.model.Manager;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;
import com.review.portal.repository.EmployeeRepository;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.repository.ReviewRepository;
import com.review.portal.service.DashboardService;
import com.review.portal.service.impl.ManagerAuthServiceImpl;
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
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Test suite for Phase 6.2 (OFFICIAL) — Enterprise HR Manager Dashboard.
 * Verifies:
 * 1. Top Header: Manager name, Manager email, Logout button, System title "Performance Review Management"
 * 2. Dynamic KPI Cards: Total Employees, Pending Reviews, Approved Reviews, Average Score
 * 3. Search Bar: Instant JavaScript filtering by employee name
 * 4. Pending Review Table: Employee ID, Employee Name, Department, Review Date, Status, Action ("Review" button linking to /manager/review/{reviewId})
 * 5. Backend integration fetching dynamically from PostgreSQL
 */
@SpringBootTest
@AutoConfigureMockMvc
class ManagerDashboardTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private DashboardService dashboardService;

    private Manager testManager;
    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testManager = managerRepository.findByEmail("manager@gmail.com")
                .orElseGet(() -> managerRepository.save(
                        Manager.builder()
                                .name("Ahmed Khan")
                                .email("manager@gmail.com")
                                .password("1234")
                                .department("IT")
                                .build()
                ));

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

        // Ensure at least one pending review exists
        if (reviewRepository.count() == 0) {
            Review pendingReview = Review.builder()
                    .employee(testEmployee)
                    .manager(testManager)
                    .technical(9)
                    .communication(8)
                    .teamwork(9)
                    .problemSolving(8)
                    .achievement("Delivered Phase 6.1 and 6.2 on schedule")
                    .futureGoal("Master full-stack automation testing")
                    .overallScore(8.5)
                    .status(ReviewStatus.PENDING)
                    .reviewDate(LocalDate.now())
                    .build();
            reviewRepository.save(pendingReview);
        }
    }

    @Test
    @DisplayName("Phase 6.2: Dashboard Service computes dynamic KPI stats directly from database")
    void testDashboardServiceStats() {
        DashboardStatsDTO stats = dashboardService.getDashboardStats();

        assertThat(stats).isNotNull();
        assertThat(stats.getTotalEmployees()).isGreaterThanOrEqualTo(1);
        assertThat(stats.getPendingReviews()).isGreaterThanOrEqualTo(1);
        assertThat(stats.getAverageScore()).isNotNull();

        List<Review> pendingQueue = dashboardService.getPendingReviews();
        assertThat(pendingQueue).isNotEmpty();
        assertThat(pendingQueue.get(0).getStatus()).isIn(ReviewStatus.PENDING, ReviewStatus.Pending);
    }

    @Test
    @DisplayName("Phase 6.2: GET /manager/dashboard renders view with Top Header, KPIs, Search Box, and Pending Table")
    void testManagerDashboardRendersAllRequiredFeatures() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_ID, testManager.getId());
        session.setAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_NAME, testManager.getName());
        session.setAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_EMAIL, testManager.getEmail());

        mockMvc.perform(get("/manager/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-dashboard"))
                .andExpect(model().attributeExists("managerName", "managerEmail", "stats", "pendingReviews"))
                // Feature 1: Top Header & System Title
                .andExpect(content().string(containsString("Performance Review Management")))
                .andExpect(content().string(containsString(testManager.getName())))
                .andExpect(content().string(containsString(testManager.getEmail())))
                .andExpect(content().string(containsString("/manager/logout")))
                // Feature 2: KPI Cards
                .andExpect(content().string(containsString("Total Employees")))
                .andExpect(content().string(containsString("Pending Reviews")))
                .andExpect(content().string(containsString("Approved Reviews")))
                .andExpect(content().string(containsString("Average Score")))
                // Feature 3: Search Bar
                .andExpect(content().string(containsString("id=\"searchEmployee\"")))
                .andExpect(content().string(containsString("filterPendingReviews()")))
                // Feature 4: Pending Review Table columns
                .andExpect(content().string(containsString("Employee ID")))
                .andExpect(content().string(containsString("Employee Name")))
                .andExpect(content().string(containsString("Department")))
                .andExpect(content().string(containsString("Review Date")))
                .andExpect(content().string(containsString("Status")))
                .andExpect(content().string(containsString("Action")))
                .andExpect(content().string(containsString("/manager/review/")))
                .andExpect(content().string(containsString("Review")));
    }
}
