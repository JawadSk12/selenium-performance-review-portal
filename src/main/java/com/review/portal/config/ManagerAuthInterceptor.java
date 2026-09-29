package com.review.portal.config;

import com.review.portal.service.impl.ManagerAuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Route protection interceptor for Manager endpoints (/manager/**).
 * Completely separate from employee interceptor.
 * Redirects unauthenticated manager requests to /manager/login.
 */
@Slf4j
@Component
public class ManagerAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();

        // Allow manager login and logout routes without interception
        if (requestUri.endsWith("/manager/login") || requestUri.endsWith("/manager/logout")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        boolean isManagerAuthenticated = (session != null && session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_ID) != null);

        if (!isManagerAuthenticated) {
            log.warn("Unauthorized manager access attempt to '{}'. Redirecting to /manager/login", requestUri);
            response.sendRedirect(request.getContextPath() + "/manager/login?unauthorized=true");
            return false;
        }

        return true;
    }
}
