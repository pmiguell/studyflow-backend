package com.studyflow.sf_backend.controller;

import com.studyflow.sf_backend.dto.request.SubjectRequestDTO;
import com.studyflow.sf_backend.dto.response.SubjectResponseDTO;
import com.studyflow.sf_backend.service.SubjectService;
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
import com.studyflow.sf_backend.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
@Tag(name = "Subjects", description = "Endpoints for managing study subjects")
@SecurityRequirement(name = "Bearer Authentication")
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @Operation(summary = "Create a new subject", description = "Creates a new subject for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SubjectResponseDTO> createSubject(
            @RequestBody
            @Parameter(description = "Subject creation details", required = true)
            SubjectRequestDTO subjectRequestDTO,
            @AuthenticationPrincipal User loggedUser) {

        SubjectResponseDTO response = subjectService.createSubject(loggedUser, subjectRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all subjects", description = "Retrieves all subjects for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subjects retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SubjectResponseDTO>> getSubjects(
            @AuthenticationPrincipal User loggedUser) {

        List<SubjectResponseDTO> responses = subjectService.getSubjectsByUser(loggedUser);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    @Operation(summary = "Search subject by title", description = "Searches for a subject by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Subject not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SubjectResponseDTO> getSubjectByTitle(
            @RequestParam
            @Parameter(description = "Subject title to search", required = true, example = "Mathematics")
            String title,
            @AuthenticationPrincipal User loggedUser) {
        SubjectResponseDTO response = subjectService.getSubjectByTitle(title, loggedUser);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update a subject", description = "Updates an existing subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Subject not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SubjectResponseDTO> updateSubject(
            @PathVariable
            @Parameter(description = "Subject ID", required = true, example = "1")
            Long id,
            @RequestBody
            @Parameter(description = "Updated subject details", required = true)
            SubjectRequestDTO dto,
            @AuthenticationPrincipal User loggedUser) {
        SubjectResponseDTO response = subjectService.updateSubject(id, dto, loggedUser);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a subject", description = "Deletes a subject and all its associated tasks and summaries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subject deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Subject not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteSubject(
            @PathVariable
            @Parameter(description = "Subject ID", required = true, example = "1")
            Long id,
            @AuthenticationPrincipal User loggedUser) {
        subjectService.deleteSubject(id, loggedUser);
        return ResponseEntity.noContent().build();
    }

}
