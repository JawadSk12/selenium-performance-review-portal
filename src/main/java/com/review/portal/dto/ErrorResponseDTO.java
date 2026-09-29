package com.review.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response model for REST and MVC exceptions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> details;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
