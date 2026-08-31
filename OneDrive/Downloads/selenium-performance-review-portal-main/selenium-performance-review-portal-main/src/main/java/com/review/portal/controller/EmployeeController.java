package com.review.portal.controller;

import com.review.portal.dto.ApiResponse;
import com.review.portal.dto.EmployeeDTO;
import com.review.portal.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller handling Employee management endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees() {
        log.info("REST request to get all employees");
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.ok("Employees retrieved successfully", employees));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long id) {
        log.info("REST request to get employee id: {}", id);
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.ok("Employee retrieved successfully", employee));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("REST request to create employee: {}", employeeDTO.getEmployeeCode());
        EmployeeDTO created = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(ApiResponse.ok("Employee created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("REST request to update employee id: {}", id);
        EmployeeDTO updated = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(ApiResponse.ok("Employee updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        log.info("REST request to delete employee id: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.ok("Employee deleted successfully", null));
    }
}
