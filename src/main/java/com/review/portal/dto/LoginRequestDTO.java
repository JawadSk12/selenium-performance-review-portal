package com.review.portal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Employee Login request payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Please provide a valid email address (e.g. employee@company.com)")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    private boolean rememberMe;
}
