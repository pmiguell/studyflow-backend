package com.studyflow.sf_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for user update request
 */
@Schema(description = "User account update details")
public record UserRequestDTO(
        @Schema(description = "New username for the account", example = "jane_doe")
        String username,

        @Schema(description = "New email address for the account", example = "jane@example.com")
        String email,

        @Schema(description = "New password for the account", example = "NewSecurePass123!")
        String password
) {
}