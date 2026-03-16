package com.studyflow.sf_backend.dto.response;

import com.studyflow.sf_backend.entity.Subject;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for subject response containing subject information
 */
@Schema(description = "Subject information")
public record SubjectResponseDTO(
        @Schema(description = "Unique identifier of the subject", example = "1")
        Long id,

        @Schema(description = "Title of the subject", example = "Advanced Mathematics")
        String title,

        @Schema(description = "Color of the subject", example = "#ff0000")
        String color
) {

    public SubjectResponseDTO(Subject subject) {
        this(subject.getId(), subject.getTitle(), subject.getColor());
    }

}