package com.review.portal.repository;

import com.review.portal.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Employee entity management.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmployeeCode(String employeeCode);

    List<Employee> findByDepartment(String department);

    List<Employee> findByManagerId(Long managerId);

    boolean existsByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);
}
