package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.dto.request.TaskRequestDTO;
import com.studyflow.sf_backend.dto.response.TaskResponseDTO;
import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.Task;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.enums.TaskStatus;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private TaskService taskService;

    private User testUser;
    private Subject testSubject;
    private Task testTask;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testSubject = new Subject();
        testSubject.setId(10L);
        testSubject.setUser(testUser);

        testTask = new Task();
        testTask.setId(100L);
        testTask.setTitle("Homework");
        testTask.setSubject(testSubject);
        testTask.setStatus(TaskStatus.NAO_INICIADO);
    }

    @Test
    void createTask_Success() {
        TaskRequestDTO dto = new TaskRequestDTO("Homework", "Desc", LocalDate.now(), false, TaskStatus.NAO_INICIADO);
        when(subjectRepository.findById(10L)).thenReturn(Optional.of(testSubject));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskResponseDTO response = taskService.createTask(10L, dto, testUser);

        assertNotNull(response);
        assertEquals("Homework", response.title());
    }

    @Test
    void createTask_SubjectNotFound_ThrowsException() {
        TaskRequestDTO dto = new TaskRequestDTO("Homework", "Desc", LocalDate.now(), false, TaskStatus.NAO_INICIADO);
        when(subjectRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> taskService.createTask(10L, dto, testUser));
    }

    @Test
    void updateTask_Success() {
        TaskRequestDTO dto = new TaskRequestDTO("Updated Homework", "Desc", LocalDate.now(), true, TaskStatus.CONCLUIDO);
        when(taskRepository.findById(100L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponseDTO response = taskService.updateTask(100L, dto, testUser);

        assertEquals("Updated Homework", response.title());
        assertEquals(TaskStatus.CONCLUIDO, response.status());
    }
}
