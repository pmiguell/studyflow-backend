package com.studyflow.sf_backend.controller;

import com.studyflow.sf_backend.dto.request.UserRequestDTO;
import com.studyflow.sf_backend.dto.response.UserResponseDTO;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.service.UserService;
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

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Endpoints for managing user account")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get user account", description = "Retrieves the authenticated user's account information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponseDTO> getAccount(@AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(userService.getAccount(loggedUser));
    }

    @PutMapping
    @Operation(summary = "Update user account", description = "Updates the authenticated user's account information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponseDTO> updateAccount(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody
            @Parameter(description = "Updated account details", required = true)
            UserRequestDTO updateRequest) {
        return ResponseEntity.ok(userService.updateUser(loggedUser.getId(), updateRequest));
    }

    @DeleteMapping
    @Operation(summary = "Delete user account", description = "Deletes the authenticated user's account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal User loggedUser) {
        userService.deleteUser(loggedUser.getId());
        return ResponseEntity.noContent().build();
    }

}

