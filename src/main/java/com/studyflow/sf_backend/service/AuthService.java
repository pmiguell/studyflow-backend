package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.dto.request.ForgotPasswordRequestDTO;
import com.studyflow.sf_backend.dto.request.LoginRequestDTO;
import com.studyflow.sf_backend.dto.request.RegisterRequestDTO;
import com.studyflow.sf_backend.dto.request.ResetPasswordRequestDTO;
import com.studyflow.sf_backend.dto.request.VerifyUserRequest;
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

    /**
     * Registers a new user and sends a verification email.
     *
     * @param registerRequestDTO contains username, email, and password for the new user
     * @throws RuntimeException if email is already registered
     */
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

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param loginRequestDTO contains email and password for authentication
     * @return JWT token string for authenticated requests
     * @throws RuntimeException if user not found, password is incorrect, or user is not verified
     */
    public String login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new RuntimeException("Usuário e/ou senha inválido(s)."));

        if(!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            throw new RuntimeException("Usuário e/ou senha inválido(s).");
        }

        if(!user.isVerified()){
            throw new RuntimeException("User is not verified.");
        }

        String token = jwtService.generateToken(user);
        return token;
    }

    /**
     * Verifies a user email address using the verification code.
     *
     * @param verifyUserRequest contains email and verification code
     * @throws RuntimeException if user not found, code is expired, or code is incorrect
     */
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
        // We can clear the code after verification for security
        user.setVerificationCode(null);
        userRepository.save(user);
    }

    /**
     * Initiates the password recovery process.
     *
     * @param requestDTO contains email of the user
     */
    public void forgotPassword(ForgotPasswordRequestDTO requestDTO) {
        // Find user by email, but don't throw an error to prevent email enumeration
        userRepository.findByEmail(requestDTO.email()).ifPresent(user -> {
            String code = String.valueOf(new Random().nextInt(900000) + 100000);
            user.setVerificationCode(code);
            user.setCodeExpirationDate(LocalDateTime.now().plusMinutes(10));
            
            userRepository.save(user);
            emailService.sendPasswordRecoveryCode(user.getEmail(), code);
        });
    }

    /**
     * Resets the user's password using the recovery code.
     *
     * @param requestDTO contains email, code, and new password
     */
    public void resetPassword(ResetPasswordRequestDTO requestDTO) {
        User user = userRepository.findByEmail(requestDTO.email())
                .orElseThrow(() -> new RuntimeException("User not found or invalid recovery attempt."));

        if (!user.isVerified()) {
            throw new RuntimeException("User email is not verified.");
        }

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(requestDTO.code())) {
            throw new RuntimeException("Invalid recovery code.");
        }

        if (user.getCodeExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Recovery code has expired.");
        }

        user.setPassword(passwordEncoder.encode(requestDTO.newPassword()));
        user.setVerificationCode(null); // Clear the code after successful reset
        userRepository.save(user);
    }
}
