package com.review.portal.config;

import com.review.portal.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor for route protection and session verification.
 * Redirects unauthenticated requests targeting protected resources to /login.
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String requestUri = request.getRequestURI();

        boolean isAuthenticated = (session != null && session.getAttribute(AuthServiceImpl.SESSION_EMPLOYEE_ID) != null);

        if (!isAuthenticated) {
            log.warn("Unauthorized access attempt to '{}'. Redirecting to /login", requestUri);
            response.sendRedirect(request.getContextPath() + "/login?unauthorized=true");
            return false;
        }

        return true;
    }
}
