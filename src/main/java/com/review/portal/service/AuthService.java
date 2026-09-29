package com.review.portal.service;

import com.review.portal.dto.LoginRequestDTO;
import com.review.portal.model.Employee;
import jakarta.servlet.http.HttpSession;

/**
 * Service interface for Employee Authentication and Session Management.
 */
public interface AuthService {

    /**
     * Authenticates employee credentials, establishes session state upon success.
     *
     * @param loginRequest Login credentials
     * @param session      Active HTTP session
     * @return Authenticated Employee entity
     */
    Employee authenticate(LoginRequestDTO loginRequest, HttpSession session);

    /**
     * Terminates the active employee session.
     *
     * @param session Active HTTP session
     */
    void logout(HttpSession session);

    /**
     * Checks if current session is authenticated.
     *
     * @param session Active HTTP session
     * @return true if authenticated, false otherwise
     */
    boolean isAuthenticated(HttpSession session);
}
