package com.studyflow.sf_backend.dto.response;

import com.studyflow.sf_backend.entity.Task;
import com.studyflow.sf_backend.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * DTO for task response containing task information
 */
@Schema(description = "Task information")
public record TaskResponseDTO(
        @Schema(description = "Unique identifier of the task", example = "1")
        Long id,

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

        @Schema(description = "ID of the subject this task belongs to", example = "1")
        Long subjectId
) {
    public TaskResponseDTO(Task task) {
        this(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.isCompleted(),
                task.getStatus(),
                task.getSubject() != null ? task.getSubject().getId() : null
        );
    }
}