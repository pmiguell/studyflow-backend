package com.studyflow.sf_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for subject creation/update request
 */
@Schema(description = "Subject creation or update details")
public record SubjectRequestDTO(
        @Schema(description = "Title of the subject", example = "Advanced Mathematics")
        String title
) {
}
