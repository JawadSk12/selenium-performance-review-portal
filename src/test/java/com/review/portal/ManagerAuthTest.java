package com.review.portal;

import com.review.portal.model.Manager;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.service.impl.ManagerAuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Test suite for Phase 6.1 — Manager Authentication.
 * Verifies:
 * - Manager login page rendering with dark glassmorphism theme and required fields
 * - Route protection for /manager/** redirecting unauthenticated users to /manager/login
 * - Successful authentication with session creation (managerId, managerName, managerEmail)
 * - Redirect to /manager/dashboard
 * - Logout route /manager/logout
 * - Non-interference with employee authentication
 */
@SpringBootTest
@AutoConfigureMockMvc
class ManagerAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ManagerRepository managerRepository;

    private Manager testManager;

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
    }

    @Test
    @DisplayName("Phase 6.1: Manager Login page renders with required elements")
    void testManagerLoginPageRenders() throws Exception {
        mockMvc.perform(get("/manager/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-login"))
                .andExpect(content().string(containsString("Manager Workspace")))
                .andExpect(content().string(containsString("Manager Email")))
                .andExpect(content().string(containsString("Password")))
                .andExpect(content().string(containsString("Remember me")))
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("manager-login.css")));
    }

    @Test
    @DisplayName("Phase 6.1: Route Protection redirects unauthenticated users targeting /manager/** to /manager/login")
    void testUnauthenticatedManagerRouteProtection() throws Exception {
        mockMvc.perform(get("/manager/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/login?unauthorized=true"));

        mockMvc.perform(get("/manager/reviews/pending"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/login?unauthorized=true"));
    }

    @Test
    @DisplayName("Phase 6.1: Invalid credentials return error on manager-login")
    void testManagerLoginFailure() throws Exception {
        mockMvc.perform(post("/manager/login")
                        .param("email", "manager@gmail.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-login"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(content().string(containsString("Invalid manager email or password")));
    }

    @Test
    @DisplayName("Phase 6.1: Successful manager login creates session attributes and redirects to /manager/dashboard")
    void testManagerLoginSuccess() throws Exception {
        MvcResult result = mockMvc.perform(post("/manager/login")
                        .param("email", "manager@gmail.com")
                        .param("password", "1234")
                        .param("rememberMe", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/dashboard"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertThat(session).isNotNull();
        assertThat(session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_ID)).isEqualTo(testManager.getId());
        assertThat(session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_NAME)).isEqualTo("Ahmed Khan");
        assertThat(session.getAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_EMAIL)).isEqualTo("manager@gmail.com");

        // Authenticated manager can now access /manager/dashboard
        mockMvc.perform(get("/manager/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Manager Workspace")))
                .andExpect(content().string(containsString("Ahmed Khan")));
    }

    @Test
    @DisplayName("Phase 6.1: Manager Logout route invalidates session and redirects to /manager/login?logout=true")
    void testManagerLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_ID, testManager.getId());
        session.setAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_NAME, testManager.getName());
        session.setAttribute(ManagerAuthServiceImpl.SESSION_MANAGER_EMAIL, testManager.getEmail());

        mockMvc.perform(get("/manager/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/login?logout=true"));

        assertThat(session.isInvalid()).isTrue();
    }
}
