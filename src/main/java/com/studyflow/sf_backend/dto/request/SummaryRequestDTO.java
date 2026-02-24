package com.studyflow.sf_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for summary creation/update request
 */
@Schema(description = "Summary creation or update details")
public record SummaryRequestDTO(
        @Schema(description = "Title of the summary", example = "Chapter 3 Summary")
        String title,

        @Schema(description = "Content of the summary", example = "This chapter covers the fundamental concepts of...")
        String content
) {
}
