package com.review.portal.controller;

import com.review.portal.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller providing system health check and portal metadata.
 */
@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("phase", "Phase 1 - Architecture & Database Foundation");
        status.put("portal", "Selenium-Tested Performance Review Portal");
        status.put("version", "1.0.0-SNAPSHOT");
        return ResponseEntity.ok(ApiResponse.ok("System is operational", status));
    }
}
