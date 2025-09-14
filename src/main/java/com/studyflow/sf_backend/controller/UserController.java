package com.studyflow.sf_backend.controller;

import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.UserRepository;
import com.studyflow.sf_backend.service.JwtService;
import com.studyflow.sf_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;   // ✅ aqui já injeta o UserService
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getAccount(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return ResponseEntity.ok(new AccountResponse(user.getUsername(), user.getEmail()));
    }

    // ✅ mover esses dois métodos para DENTRO da classe
    @PutMapping
    public ResponseEntity<User> updateAccount(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody User updateRequest) {

        User updatedUser = userService.updateUser(
                loggedUser.getId(),
                updateRequest.getUsername(),
                updateRequest.getEmail(),
                updateRequest.getPassword()
        );

        updatedUser.setPassword(null); // nunca retorna senha
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal User loggedUser) {
        userService.deleteUser(loggedUser.getId());
        return ResponseEntity.noContent().build();
    }

    // DTO simples para resposta
    record AccountResponse(String username, String email) {}
}

