package com.studyflow.sf_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studyflow.sf_backend.dto.request.TaskRequestDTO;
import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.Task;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.enums.TaskStatus;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.TaskRepository;
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

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TaskRepository taskRepository;

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
        objectMapper.registerModule(new JavaTimeModule());

        doNothing().when(emailService).sendVerificationCode(anyString(), anyString());

        testUser = new User();
        testUser.setUsername("taskuser");
        testUser.setEmail("task@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setVerified(true);
        testUser = userRepository.save(testUser);

        token = "Bearer " + jwtService.generateToken(testUser);

        testSubject = new Subject();
        testSubject.setTitle("Math");
        testSubject.setUser(testUser);
        testSubject = subjectRepository.save(testSubject);
    }

    @Test
    void shouldCreateTask() throws Exception {
        TaskRequestDTO request = new TaskRequestDTO("Read Chapter 1", "desc", LocalDate.now().plusDays(2), false, TaskStatus.NAO_INICIADO);

        mockMvc.perform(post("/tasks/subject/" + testSubject.getId())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Read Chapter 1"));
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        Task t1 = new Task();
        t1.setTitle("T1");
        t1.setSubject(testSubject);
        t1.setStatus(TaskStatus.NAO_INICIADO);
        t1.setDeadline(LocalDate.now());
        taskRepository.save(t1);

        mockMvc.perform(get("/tasks")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("T1"));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task t1 = new Task();
        t1.setTitle("Old Title");
        t1.setSubject(testSubject);
        t1.setStatus(TaskStatus.NAO_INICIADO);
        t1.setDeadline(LocalDate.now());
        t1 = taskRepository.save(t1);

        TaskRequestDTO updateRequest = new TaskRequestDTO("New Title", "New Desc", LocalDate.now(), true, TaskStatus.CONCLUIDO);

        mockMvc.perform(put("/tasks/" + t1.getId())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.status").value("CONCLUIDO"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Task t1 = new Task();
        t1.setTitle("To Delete");
        t1.setSubject(testSubject);
        t1.setStatus(TaskStatus.NAO_INICIADO);
        t1.setDeadline(LocalDate.now());
        t1 = taskRepository.save(t1);

        mockMvc.perform(delete("/tasks/" + t1.getId())
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
