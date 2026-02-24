package com.studyflow.sf_backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for login response containing JWT token
 */
@Schema(description = "Login response with authentication token")
public record LoginResponseDTO(
        @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {
}
