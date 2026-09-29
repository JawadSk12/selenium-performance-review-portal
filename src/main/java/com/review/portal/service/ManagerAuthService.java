package com.review.portal.service;

import com.review.portal.dto.LoginRequestDTO;
import com.review.portal.model.Manager;
import jakarta.servlet.http.HttpSession;

/**
 * Service interface for Manager Authentication and Session Management.
 * Completely separate from employee authentication.
 */
public interface ManagerAuthService {

    /**
     * Authenticates manager credentials, establishes manager session attributes upon success.
     *
     * @param loginRequest Manager credentials
     * @param session      Active HTTP session
     * @return Authenticated Manager entity
     */
    Manager authenticate(LoginRequestDTO loginRequest, HttpSession session);

    /**
     * Terminates the active manager session.
     *
     * @param session Active HTTP session
     */
    void logout(HttpSession session);

    /**
     * Checks if current session has an authenticated manager.
     *
     * @param session Active HTTP session
     * @return true if authenticated as manager, false otherwise
     */
    boolean isAuthenticated(HttpSession session);
}
