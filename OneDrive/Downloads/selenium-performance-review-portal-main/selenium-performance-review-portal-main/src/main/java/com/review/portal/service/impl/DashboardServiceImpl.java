package com.review.portal.service.impl;

import com.review.portal.dto.DashboardStatsDTO;
import com.review.portal.model.Manager;
import com.review.portal.model.Review;
import com.review.portal.model.ReviewStatus;
import com.review.portal.repository.EmployeeRepository;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.repository.ReviewRepository;
import com.review.portal.service.DashboardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of DashboardService for Manager Dashboard KPI calculation
 * and pending reviews queue processing (Phase 6.2).
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final ReviewRepository reviewRepository;
    private final ManagerRepository managerRepository;

    private static final List<ReviewStatus> PENDING_STATUSES = List.of(ReviewStatus.PENDING, ReviewStatus.Pending);

    @Override
    public DashboardStatsDTO getDashboardStats() {
        log.info("Computing enterprise dashboard metrics dynamically from PostgreSQL");

        long totalEmployees = employeeRepository.count();
        long pendingReviews = reviewRepository.countByStatusIn(PENDING_STATUSES);
        long approvedReviews = reviewRepository.countByStatus(ReviewStatus.APPROVED);

        Double averageScore = reviewRepository.getAverageScore();
        if (averageScore == null || averageScore.isNaN()) {
            averageScore = 0.00;
        } else {
            averageScore = Math.round(averageScore * 100.0) / 100.0;
        }

        log.info("Dashboard stats computed - Employees: {}, Pending: {}, Approved: {}, AvgScore: {}",
                totalEmployees, pendingReviews, approvedReviews, averageScore);

        return DashboardStatsDTO.builder()
                .totalEmployees(totalEmployees)
                .pendingReviews(pendingReviews)
                .approvedReviews(approvedReviews)
                .averageScore(averageScore)
                .build();
    }

    @Override
    public List<Review> getPendingReviews() {
        log.info("Fetching pending reviews queue dynamically from PostgreSQL");
        return reviewRepository.findByStatusInOrderByReviewDateDesc(PENDING_STATUSES);
    }

    @Override
    @Transactional
    public Manager getCurrentManager(HttpSession session) {
        if (session != null) {
            Object mgrIdObj = session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_ID);
            if (mgrIdObj != null) {
                Long mgrId = (mgrIdObj instanceof Long) ? (Long) mgrIdObj : Long.valueOf(mgrIdObj.toString());
                return managerRepository.findById(mgrId)
                        .orElseGet(() -> resolveDefaultManager(session));
            }

            Object emailObj = session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_EMAIL);
            if (emailObj != null) {
                return managerRepository.findByEmail(emailObj.toString())
                        .orElseGet(() -> resolveDefaultManager(session));
            }
        }
        return resolveDefaultManager(session);
    }

    private Manager resolveDefaultManager(HttpSession session) {
        Manager defaultManager = managerRepository.findByEmail("manager@gmail.com")
                .orElseGet(() -> managerRepository.findAll().stream().findFirst()
                        .orElseGet(() -> {
                            Manager newMgr = Manager.builder()
                                    .name("Ahmed Khan")
                                    .email("manager@gmail.com")
                                    .password("1234")
                                    .department("IT")
                                    .build();
                            return managerRepository.save(newMgr);
                        }));

        if (session != null && defaultManager != null) {
            session.setAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_ID, defaultManager.getId());
            session.setAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_NAME, defaultManager.getName());
            session.setAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_EMAIL, defaultManager.getEmail());
        }

        return defaultManager;
    }
}
