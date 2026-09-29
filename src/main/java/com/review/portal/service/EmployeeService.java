package com.review.portal.service;

import com.review.portal.dto.EmployeeDTO;
import com.review.portal.model.Employee;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * Service interface for Employee domain operations and dashboard management.
 */
public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById(Long id);

    EmployeeDTO getEmployeeByCode(String employeeCode);

    List<EmployeeDTO> getEmployeesByDepartment(String department);

    List<EmployeeDTO> getEmployeesByManager(Long managerId);

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    void deleteEmployee(Long id);

    /**
     * Resolves the current Employee entity from the HTTP session.
     * Fallbacks to the default demo user if session is not yet populated.
     */
    Employee getCurrentEmployee(HttpSession session);

    /**
     * Finds Employee entity by primary ID.
     */
    Employee findById(Long id);

    /**
     * Finds Employee entity by email address.
     */
    Employee findByEmail(String email);
}
