package com.studyflow.sf_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for user registration request
 */
@Schema(description = "User registration details")
public record RegisterRequestDTO(
        @Schema(description = "Username for the account", example = "john_doe")
        String username,

        @Schema(description = "Email address for the account", example = "john@example.com")
        String email,

        @Schema(description = "Password for the account", example = "SecurePass123!")
        String password
) {
}
