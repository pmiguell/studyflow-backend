package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.dto.LoginRequestDTO;
import com.studyflow.sf_backend.dto.RegisterRequestDTO;
import com.studyflow.sf_backend.dto.VerifyUserRequest;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    public void register(RegisterRequestDTO registerRequestDTO) {
        if(userRepository.findByEmail(registerRequestDTO.email()).isPresent()) {
            throw new RuntimeException("Email already registered.");
        }

        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        User user = new User();
        user.setUsername(registerRequestDTO.username());
        user.setEmail(registerRequestDTO.email());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        user.setVerificationCode(code);
        user.setCodeExpirationDate(LocalDateTime.now().plusMinutes(10));
        user.setVerified(false);

        emailService.sendVerificationCode(user.getEmail(), code);

        userRepository.save(user);
    }

    public String login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if(!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            throw new RuntimeException("Incorrect password.");
        }

        if(!user.isVerified()){
            throw new RuntimeException("User is not verified.");
        }

        String token = jwtService.generateToken(user);
        return token;
    }

    public void verify(VerifyUserRequest verifyUserRequest) {
        User user = userRepository.findByEmail(verifyUserRequest.email())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if(user.getCodeExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Confirmation code is expired.");
        }

        if(!user.getVerificationCode().equals(verifyUserRequest.verificationCode())){
            throw new RuntimeException("Confirmation code is incorrect.");
        }

        user.setVerified(true);
        userRepository.save(user);
    }
}
