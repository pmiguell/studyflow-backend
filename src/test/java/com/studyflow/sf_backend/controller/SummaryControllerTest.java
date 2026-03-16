package com.studyflow.sf_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyflow.sf_backend.dto.request.SummaryRequestDTO;
import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.Summary;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.SummaryRepository;
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
class SummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @MockBean
    private EmailService emailService;

    private String token;
    private User testUser;
    private Subject testSubject;

    @BeforeEach
    void setup() {
        doNothing().when(emailService).sendVerificationCode(anyString(), anyString());

        testUser = new User();
        testUser.setUsername("summaryuser");
        testUser.setEmail("summary@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setVerified(true);
        testUser = userRepository.save(testUser);

        token = "Bearer " + jwtService.generateToken(testUser);

        testSubject = new Subject();
        testSubject.setTitle("Biology");
        testSubject.setUser(testUser);
        testSubject = subjectRepository.save(testSubject);
    }

    @Test
    void shouldCreateSummary() throws Exception {
        SummaryRequestDTO request = new SummaryRequestDTO("Chapter 1", "Content of chap 1");

        mockMvc.perform(post("/summaries/subject/" + testSubject.getId())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Chapter 1"));
    }

    @Test
    void shouldGetAllSummaries() throws Exception {
        Summary s1 = new Summary();
        s1.setTitle("Sub1");
        s1.setContent("C1");
        s1.setSubject(testSubject);
        summaryRepository.save(s1);

        mockMvc.perform(get("/summaries")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sub1"));
    }

    @Test
    void shouldUpdateSummary() throws Exception {
        Summary s1 = new Summary();
        s1.setTitle("Old Title");
        s1.setContent("Old Content");
        s1.setSubject(testSubject);
        s1 = summaryRepository.save(s1);

        SummaryRequestDTO updateRequest = new SummaryRequestDTO("New Title", "New Content");

        mockMvc.perform(put("/summaries/" + s1.getId())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void shouldDeleteSummary() throws Exception {
        Summary s1 = new Summary();
        s1.setTitle("To Delete");
        s1.setContent("C");
        s1.setSubject(testSubject);
        s1 = summaryRepository.save(s1);

        mockMvc.perform(delete("/summaries/" + s1.getId())
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
