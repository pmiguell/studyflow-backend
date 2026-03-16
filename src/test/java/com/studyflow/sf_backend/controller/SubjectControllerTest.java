package com.studyflow.sf_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyflow.sf_backend.dto.request.SubjectRequestDTO;
import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.UserRepository;
import com.studyflow.sf_backend.service.JwtService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

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
        testUser.setUsername("subjectuser");
        testUser.setEmail("subject@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setVerified(true);
        testUser = userRepository.save(testUser);

        token = "Bearer " + jwtService.generateToken(testUser);
    }

    @Test
    void shouldCreateSubject() throws Exception {
        SubjectRequestDTO request = new SubjectRequestDTO("Mathematics");

        mockMvc.perform(post("/subjects")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Mathematics"));
    }

    @Test
    void shouldFailCreateSubjectUnauthenticated() throws Exception {
        SubjectRequestDTO request = new SubjectRequestDTO("Physics");

        mockMvc.perform(post("/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldGetAllSubjects() throws Exception {
        Subject s1 = new Subject();
        s1.setTitle("Sub1");
        s1.setUser(testUser);
        subjectRepository.save(s1);

        mockMvc.perform(get("/subjects")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sub1"));
    }

    @Test
    void shouldSearchSubjectByTitle() throws Exception {
        Subject s1 = new Subject();
        s1.setTitle("Biology");
        s1.setUser(testUser);
        subjectRepository.save(s1);

        mockMvc.perform(get("/subjects/search?title=Biology")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Biology"));
    }

    @Test
    void shouldUpdateSubject() throws Exception {
        Subject s1 = new Subject();
        s1.setTitle("Old Title");
        s1.setUser(testUser);
        s1 = subjectRepository.save(s1);

        SubjectRequestDTO updateRequest = new SubjectRequestDTO("New Title");

        mockMvc.perform(put("/subjects/" + s1.getId())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void shouldDeleteSubject() throws Exception {
        Subject s1 = new Subject();
        s1.setTitle("To Delete");
        s1.setUser(testUser);
        s1 = subjectRepository.save(s1);

        mockMvc.perform(delete("/subjects/" + s1.getId())
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
