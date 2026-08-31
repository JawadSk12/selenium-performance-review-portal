package com.review.portal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an Employee who creates performance reviews and belongs to a department.
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"reviews", "manager"})
@EqualsAndHashCode(exclude = {"reviews", "manager"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long id;

    @NotBlank(message = "Employee code is required")
    @Column(name = "employee_code", nullable = false, unique = true, length = 50)
    private String employeeCode;

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Employee email is required")
    @Email(message = "Please provide a valid email address")
    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @NotBlank(message = "Department is required")
    @Column(name = "department", nullable = false, length = 80)
    private String department;

    @Column(name = "designation", length = 80)
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Builder.Default
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
}
