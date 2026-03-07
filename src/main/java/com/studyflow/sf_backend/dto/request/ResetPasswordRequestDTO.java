package com.studyflow.sf_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data Transfer Object for reset password requests")
public record ResetPasswordRequestDTO(
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Schema(description = "User's email address", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank(message = "Verification code is required")
        @Schema(description = "Code sent to the user's email", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
        String code,

        @NotBlank(message = "New password is required")
        @Size(min = 6, message = "Password must have at least 6 characters")
        @Schema(description = "New user password", example = "newP@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED)
        String newPassword
) {}
