package com.studyflow.sf_backend.controller;

import com.studyflow.sf_backend.dto.request.SummaryRequestDTO;
import com.studyflow.sf_backend.dto.response.SummaryResponseDTO;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.service.SummaryService;
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
@RequestMapping("/summaries")
@RequiredArgsConstructor
@Tag(name = "Summaries", description = "Endpoints for managing study summaries")
@SecurityRequirement(name = "Bearer Authentication")
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping
    @Operation(summary = "Get all summaries", description = "Retrieves all summaries for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Summaries retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SummaryResponseDTO>> getAllSummaries(
            @AuthenticationPrincipal User loggedUser) {

        return ResponseEntity.ok(summaryService.getSummariesByUser(loggedUser));
    }

    @PostMapping("/subject/{subjectId}")
    @Operation(summary = "Create a new summary", description = "Creates a new summary for a specific subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Summary created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Subject not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SummaryResponseDTO> createSummary(
            @PathVariable
            @Parameter(description = "Subject ID", required = true, example = "1")
            Long subjectId,
            @RequestBody
            @Parameter(description = "Summary creation details", required = true)
            SummaryRequestDTO dto,
            @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(summaryService.createSummary(subjectId, dto, loggedUser));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a summary", description = "Updates an existing summary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Summary updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Summary not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SummaryResponseDTO> updateSummary(
            @PathVariable
            @Parameter(description = "Summary ID", required = true, example = "1")
            Long id,
            @RequestBody
            @Parameter(description = "Updated summary details", required = true)
            SummaryRequestDTO dto,
            @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(summaryService.updateSummary(id, dto, loggedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a summary", description = "Deletes a summary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Summary deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Summary not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteSummary(
            @PathVariable
            @Parameter(description = "Summary ID", required = true, example = "1")
            Long id,
            @AuthenticationPrincipal User loggedUser) {
        summaryService.deleteSummary(id, loggedUser);
        return ResponseEntity.noContent().build();
    }

}
