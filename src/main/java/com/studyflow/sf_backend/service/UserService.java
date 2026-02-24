package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.dto.request.UserRequestDTO;
import com.studyflow.sf_backend.dto.response.UserResponseDTO;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves the account information for the authenticated user.
     *
     * @param loggedUser the authenticated user entity
     * @return UserResponseDTO containing user id, username, and email
     */
    public UserResponseDTO getAccount(User loggedUser) {
        return new UserResponseDTO(loggedUser);
    }

    /**
     * Updates the user account information.
     *
     * @param userId the id of the user to update
     * @param dto contains optional new username, email, and password; null/blank values are ignored
     * @return UserResponseDTO with updated user information
     * @throws ResponseStatusException with HTTP 404 if user not found
     */
    public UserResponseDTO updateUser(Long userId, UserRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (dto.username() != null && !dto.username().isBlank()) {
            user.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            user.setEmail(dto.email());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        User saved = userRepository.save(user);
        return new UserResponseDTO(saved);
    }

    /**
     * Deletes a user account and all associated data.
     *
     * @param userId the id of the user to delete
     * @throws ResponseStatusException with HTTP 404 if user not found
     */
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        userRepository.delete(user);
    }

}
