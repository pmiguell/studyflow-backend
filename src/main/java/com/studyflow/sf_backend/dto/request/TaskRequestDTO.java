package com.studyflow.sf_backend.dto.request;

import com.studyflow.sf_backend.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * DTO for task creation/update request
 */
@Schema(description = "Task creation or update details")
public record TaskRequestDTO(
        @Schema(description = "Title of the task", example = "Complete chapter 3")
        String title,

        @Schema(description = "Detailed description of the task", example = "Read and summarize chapter 3 of the textbook")
        String description,

        @Schema(description = "Deadline for the task", example = "2025-03-15")
        LocalDate deadline,

        @Schema(description = "Whether the task is completed", example = "false")
        boolean completed,

        @Schema(description = "Current status of the task")
        TaskStatus status,

        @Schema(description = "Optional ID of the subject this task belongs to", example = "1")
        Long subjectId
) {}