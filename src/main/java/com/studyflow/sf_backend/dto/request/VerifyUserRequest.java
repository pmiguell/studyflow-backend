package com.studyflow.sf_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for email verification request
 */
@Schema(description = "Email verification details")
public record VerifyUserRequest(
        @Schema(description = "User email address to verify", example = "user@example.com")
        String email,

        @Schema(description = "Verification code sent to the user's email", example = "123456")
        String verificationCode
) {
}
