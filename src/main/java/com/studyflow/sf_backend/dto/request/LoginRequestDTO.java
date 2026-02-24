package com.studyflow.sf_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for user login request
 */
@Schema(description = "User login credentials")
public record LoginRequestDTO(
        @Schema(description = "User email address", example = "user@example.com")
        String email,

        @Schema(description = "User password", example = "password123")
        String password
) {
}
