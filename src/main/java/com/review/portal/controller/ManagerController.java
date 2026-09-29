package com.review.portal.controller;

import com.review.portal.dto.ApiResponse;
import com.review.portal.dto.ManagerDTO;
import com.review.portal.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller handling Manager management endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ManagerDTO>>> getAllManagers() {
        log.info("REST request to get all managers");
        List<ManagerDTO> managers = managerService.getAllManagers();
        return ResponseEntity.ok(ApiResponse.ok("Managers retrieved successfully", managers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ManagerDTO>> getManagerById(@PathVariable Long id) {
        log.info("REST request to get manager id: {}", id);
        ManagerDTO manager = managerService.getManagerById(id);
        return ResponseEntity.ok(ApiResponse.ok("Manager retrieved successfully", manager));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ManagerDTO>> createManager(@Valid @RequestBody ManagerDTO managerDTO) {
        log.info("REST request to create manager: {}", managerDTO.getEmail());
        ManagerDTO created = managerService.createManager(managerDTO);
        return new ResponseEntity<>(ApiResponse.ok("Manager created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ManagerDTO>> updateManager(
            @PathVariable Long id,
            @Valid @RequestBody ManagerDTO managerDTO) {
        log.info("REST request to update manager id: {}", id);
        ManagerDTO updated = managerService.updateManager(id, managerDTO);
        return ResponseEntity.ok(ApiResponse.ok("Manager updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteManager(@PathVariable Long id) {
        log.info("REST request to delete manager id: {}", id);
        managerService.deleteManager(id);
        return ResponseEntity.ok(ApiResponse.ok("Manager deleted successfully", null));
    }
}
