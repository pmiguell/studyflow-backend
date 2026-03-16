package com.studyflow.sf_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyflow.sf_backend.dto.request.UserRequestDTO;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.UserRepository;
import com.studyflow.sf_backend.service.EmailService;
import com.studyflow.sf_backend.service.JwtService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @MockBean
    private EmailService emailService;

    private String token;
    private User testUser;

    @BeforeEach
    void setup() {
        doNothing().when(emailService).sendVerificationCode(anyString(), anyString());

        testUser = new User();
        testUser.setUsername("acctuser");
        testUser.setEmail("acct@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setVerified(true);
        testUser = userRepository.save(testUser);

        token = "Bearer " + jwtService.generateToken(testUser);
    }

    @Test
    void shouldGetAccount() throws Exception {
        mockMvc.perform(get("/account")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("acctuser"));
    }

    @Test
    void shouldUpdateAccount() throws Exception {
        UserRequestDTO updateRequest = new UserRequestDTO("updateduser", "updated@example.com", "newpass123");

        mockMvc.perform(put("/account")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"));
    }

    @Test
    void shouldDeleteAccount() throws Exception {
        mockMvc.perform(delete("/account")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
