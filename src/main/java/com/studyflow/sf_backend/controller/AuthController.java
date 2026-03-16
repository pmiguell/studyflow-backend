package com.studyflow.sf_backend.controller;

import com.studyflow.sf_backend.dto.request.ForgotPasswordRequestDTO;
import com.studyflow.sf_backend.dto.request.LoginRequestDTO;
import com.studyflow.sf_backend.dto.request.RegisterRequestDTO;
import com.studyflow.sf_backend.dto.request.ResetPasswordRequestDTO;
import com.studyflow.sf_backend.dto.request.VerifyUserRequest;
import com.studyflow.sf_backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and sends verification email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> register(
            @RequestBody
            @Parameter(description = "User registration details", required = true)
            RegisterRequestDTO registerRequestDTO) {
        authService.register(registerRequestDTO);
        return ResponseEntity.ok("User registered successfully. Verify your email.");
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates user credentials and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, returns JWT token",
                    content = @Content(schema = @Schema(example = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> login(
            @RequestBody
            @Parameter(description = "User login credentials", required = true)
            LoginRequestDTO loginRequestDTO) {
        try {
            String token = authService.login(loginRequestDTO);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify user email", description = "Verifies user email address using verification code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired verification code"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> verify(
            @RequestBody
            @Parameter(description = "User verification details", required = true)
            VerifyUserRequest verifyUserRequest) {
        authService.verify(verifyUserRequest);
        return ResponseEntity.ok("User verified successfully.");
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Initiates password recovery process by sending a code to user's email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recovery email sent successfuly (or ignored if email not found)"),
            @ApiResponse(responseCode = "400", description = "Invalid request format")
    })
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO requestDTO) {
        authService.forgotPassword(requestDTO);
        return ResponseEntity.ok("If an account with this email exists, a password recovery code has been sent.");
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Resets user password using the recovery code sent via email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid code, expired code, or missing parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO requestDTO) {
        try {
            authService.resetPassword(requestDTO);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
