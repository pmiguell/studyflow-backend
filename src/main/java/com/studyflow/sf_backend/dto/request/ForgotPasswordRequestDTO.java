package com.studyflow.sf_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data Transfer Object for forgot password requests")
public record ForgotPasswordRequestDTO(
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Schema(description = "User's email address", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email
) {}
