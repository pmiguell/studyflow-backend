package com.studyflow.sf_backend.dto.response;

import com.studyflow.sf_backend.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for user response containing account information
 */
@Schema(description = "User account information")
public record UserResponseDTO(
        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,

        @Schema(description = "Username of the account", example = "john_doe")
        String username,

        @Schema(description = "Email address of the account", example = "john@example.com")
        String email
) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getUsername(), user.getEmail());
    }

}