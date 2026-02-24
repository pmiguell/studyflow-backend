package com.studyflow.sf_backend.dto.response;

import com.studyflow.sf_backend.entity.Summary;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for summary response containing summary information
 */
@Schema(description = "Summary information")
public record SummaryResponseDTO(
        @Schema(description = "Unique identifier of the summary", example = "1")
        Long id,

        @Schema(description = "Title of the summary", example = "Chapter 3 Summary")
        String title,

        @Schema(description = "Content of the summary", example = "This chapter covers the fundamental concepts of...")
        String content,

        @Schema(description = "ID of the subject this summary belongs to", example = "1")
        Long subjectId
) {

    public SummaryResponseDTO(Summary summary) {
        this(summary.getId(), summary.getTitle(), summary.getContent(), summary.getSubject().getId());
    }

}