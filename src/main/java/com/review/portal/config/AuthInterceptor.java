package com.review.portal.config;

import com.review.portal.model.Employee;
import com.review.portal.service.EmployeeService;
import com.review.portal.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor for route protection and session verification.
 * Redirects unauthenticated requests targeting protected resources to /login.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final EmployeeService employeeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(true);
        String requestUri = request.getRequestURI();

        boolean hasSessionAuth = (session.getAttribute(AuthServiceImpl.SESSION_EMPLOYEE_ID) != null);

        if (!hasSessionAuth) {
            // Resolve demo user fallback if available
            Employee employee = employeeService.getCurrentEmployee(session);
            if (employee == null) {
                log.warn("Unauthorized access attempt to '{}'. Redirecting to /login", requestUri);
                response.sendRedirect(request.getContextPath() + "/login?unauthorized=true");
                return false;
            }
        }

        return true;
    }
}
