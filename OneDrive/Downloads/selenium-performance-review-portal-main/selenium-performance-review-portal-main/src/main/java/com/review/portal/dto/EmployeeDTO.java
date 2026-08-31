package com.review.portal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Employee operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "Employee code is required")
    private String employeeCode;

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Employee email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Department is required")
    private String department;

    private String designation;

    private Long managerId;

    private String managerName;
}
