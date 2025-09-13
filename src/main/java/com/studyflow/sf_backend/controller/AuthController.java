package com.studyflow.sf_backend.controller;

import com.studyflow.sf_backend.dto.LoginRequestDTO;
import com.studyflow.sf_backend.dto.RegisterRequestDTO;
import com.studyflow.sf_backend.dto.VerifyUserRequest;
import com.studyflow.sf_backend.service.AuthService;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        authService.register(registerRequestDTO);
        return ResponseEntity.ok("User registered successfully. Verify your email.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String token = authService.login(loginRequestDTO);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyUserRequest verifyUserRequest) {
        authService.verify(verifyUserRequest);
        return ResponseEntity.ok("User verified successfully.");
    }
}
