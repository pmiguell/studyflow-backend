package com.studyflow.sf_backend.controller;

import com.studyflow.sf_backend.dto.request.TaskRequestDTO;
import com.studyflow.sf_backend.dto.response.TaskResponseDTO;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints for managing study tasks")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieves all tasks for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(taskService.getTasksByUser(loggedUser));
    }

    @PostMapping
    @Operation(summary = "Create a general task", description = "Creates a new task without explicitly requiring a subject in the path")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TaskResponseDTO> createGeneralTask(
            @RequestBody
            @Parameter(description = "Task creation details", required = true)
            TaskRequestDTO dto,
            @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(taskService.createTask(dto.subjectId(), dto, loggedUser));
    }

    @PostMapping("/subject/{subjectId}")
    @Operation(summary = "Create a new task", description = "Creates a new task for a specific subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Subject not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TaskResponseDTO> createTask(
            @PathVariable
            @Parameter(description = "Subject ID", required = true, example = "1")
            Long subjectId,
            @RequestBody
            @Parameter(description = "Task creation details", required = true)
            TaskRequestDTO dto,
            @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(taskService.createTask(subjectId, dto, loggedUser));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Updates an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable
            @Parameter(description = "Task ID", required = true, example = "1")
            Long id,
            @RequestBody
            @Parameter(description = "Updated task details", required = true)
            TaskRequestDTO dto,
            @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(taskService.updateTask(id, dto, loggedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Deletes a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteTask(
            @PathVariable
            @Parameter(description = "Task ID", required = true, example = "1")
            Long id,
            @AuthenticationPrincipal User loggedUser) {
        taskService.deleteTask(id, loggedUser);
        return ResponseEntity.noContent().build();
    }

}
