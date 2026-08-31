package com.review.portal.service;

import com.review.portal.dto.EmployeeDTO;

import java.util.List;

/**
 * Service interface for Employee domain operations.
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
}
