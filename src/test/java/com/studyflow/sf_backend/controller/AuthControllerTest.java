package com.studyflow.sf_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyflow.sf_backend.dto.request.LoginRequestDTO;
import com.studyflow.sf_backend.dto.request.RegisterRequestDTO;
import com.studyflow.sf_backend.dto.request.VerifyUserRequest;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.UserRepository;
import com.studyflow.sf_backend.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    @BeforeEach
    void setup() {
        doNothing().when(emailService).sendVerificationCode(anyString(), anyString());
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("newuser", "new@example.com", "password123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailRegisterWithDuplicateEmail() throws Exception {
        User user = new User();
        user.setUsername("existing");
        user.setEmail("existing@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);

        RegisterRequestDTO request = new RegisterRequestDTO("newuser2", "existing@example.com", "password123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailRegisterWithDuplicateUsername() throws Exception {
        User user = new User();
        user.setUsername("existing");
        user.setEmail("existing@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);

        RegisterRequestDTO request = new RegisterRequestDTO("existing", "new@example.com", "password123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        User user = new User();
        user.setUsername("loginuser");
        user.setEmail("login@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setVerified(true);
        userRepository.save(user);

        LoginRequestDTO request = new LoginRequestDTO("login@example.com", "password123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldVerifyUserSuccessfully() throws Exception {
        User user = new User();
        user.setUsername("verifyuser");
        user.setEmail("verify@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setVerified(false);
        user.setVerificationCode("123456");
        user.setCodeExpirationDate(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        VerifyUserRequest request = new VerifyUserRequest("verify@example.com", "123456");

        mockMvc.perform(post("/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
