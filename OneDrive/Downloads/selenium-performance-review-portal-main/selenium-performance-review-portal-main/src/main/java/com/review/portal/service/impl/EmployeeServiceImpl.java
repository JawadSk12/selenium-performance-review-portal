package com.review.portal.service.impl;

import com.review.portal.dto.EmployeeDTO;
import com.review.portal.exception.ResourceNotFoundException;
import com.review.portal.model.Employee;
import com.review.portal.repository.EmployeeRepository;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of EmployeeService for employee domain and dashboard profile management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        log.info("Fetching employee by id: {}", id);
        Employee employee = findById(id);
        return mapToDTO(employee);
    }

    @Override
    public EmployeeDTO getEmployeeByCode(String employeeCode) {
        log.info("Fetching employee by code: {}", employeeCode);
        return employeeRepository.findByEmployeeCode(employeeCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        log.info("Fetching employees by department: {}", department);
        return employeeRepository.findByDepartment(department).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByManager(Long managerId) {
        log.info("Fetching employees for manager id: {}", managerId);
        return employeeRepository.findAll().stream()
                .filter(e -> e.getManager() != null && e.getManager().getId().equals(managerId))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        log.info("Creating new employee: {}", employeeDTO.getName());
        Employee employee = Employee.builder()
                .name(employeeDTO.getName())
                .email(employeeDTO.getEmail())
                .department(employeeDTO.getDepartment())
                .designation(employeeDTO.getDesignation())
                .employeeCode(employeeDTO.getEmployeeCode() != null ? employeeDTO.getEmployeeCode() : "EMP-" + System.currentTimeMillis())
                .password("1234")
                .build();

        Employee saved = employeeRepository.save(employee);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        log.info("Updating employee id: {}", id);
        Employee employee = findById(id);
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setDesignation(employeeDTO.getDesignation());
        Employee updated = employeeRepository.save(employee);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Deleting employee id: {}", id);
        Employee employee = findById(id);
        employeeRepository.delete(employee);
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
    }

    @Override
    @Transactional
    public Employee getCurrentEmployee(HttpSession session) {
        if (session != null) {
            Object empIdObj = session.getAttribute(AuthServiceImpl.SESSION_EMPLOYEE_ID);
            if (empIdObj != null) {
                Long empId = (empIdObj instanceof Long) ? (Long) empIdObj : Long.valueOf(empIdObj.toString());
                return employeeRepository.findById(empId)
                        .orElseGet(() -> resolveDefaultEmployee(session));
            }

            Object emailObj = session.getAttribute(AuthServiceImpl.SESSION_EMPLOYEE_EMAIL);
            if (emailObj != null) {
                return employeeRepository.findByEmail(emailObj.toString())
                        .orElseGet(() -> resolveDefaultEmployee(session));
            }
        }
        return resolveDefaultEmployee(session);
    }

    private Employee resolveDefaultEmployee(HttpSession session) {
        // Prioritize seeded or existing user "jawad@gmail.com"
        Employee defaultEmployee = employeeRepository.findByEmail("jawad@gmail.com")
                .orElseGet(() -> employeeRepository.findAll().stream().findFirst()
                        .orElseGet(() -> {
                            Employee newEmp = Employee.builder()
                                    .name("Jawad Shaikh")
                                    .email("jawad@gmail.com")
                                    .password("1234")
                                    .department("IT")
                                    .designation("Software Intern")
                                    .employeeCode("EMP-1001")
                                    .build();
                            return employeeRepository.save(newEmp);
                        }));

        if (session != null && defaultEmployee != null) {
            session.setAttribute(AuthServiceImpl.SESSION_EMPLOYEE_ID, defaultEmployee.getId());
            session.setAttribute(AuthServiceImpl.SESSION_EMPLOYEE_NAME, defaultEmployee.getName());
            session.setAttribute(AuthServiceImpl.SESSION_EMPLOYEE_EMAIL, defaultEmployee.getEmail());
            session.setAttribute(AuthServiceImpl.SESSION_EMPLOYEE_DEPT, defaultEmployee.getDepartment());
            session.setAttribute(AuthServiceImpl.SESSION_EMPLOYEE_ROLE, defaultEmployee.getDesignation());
        }

        return defaultEmployee;
    }

    private EmployeeDTO mapToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .designation(employee.getDesignation())
                .managerId(employee.getManager() != null ? employee.getManager().getId() : null)
                .managerName(employee.getManager() != null ? employee.getManager().getName() : null)
                .build();
    }
}
