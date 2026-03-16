package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.dto.request.SubjectRequestDTO;
import com.studyflow.sf_backend.dto.response.SubjectResponseDTO;
import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    private User testUser;
    private Subject testSubject;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testSubject = new Subject();
        testSubject.setId(10L);
        testSubject.setTitle("Math");
        testSubject.setUser(testUser);
    }

    @Test
    void createSubject_Success() {
        SubjectRequestDTO dto = new SubjectRequestDTO("Math");
        when(subjectRepository.save(any(Subject.class))).thenReturn(testSubject);

        SubjectResponseDTO response = subjectService.createSubject(testUser, dto);

        assertNotNull(response);
        assertEquals("Math", response.title());
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void getSubjectsByUser_Success() {
        when(subjectRepository.findByUser(testUser)).thenReturn(List.of(testSubject));

        List<SubjectResponseDTO> response = subjectService.getSubjectsByUser(testUser);

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("Math", response.get(0).title());
    }

    @Test
    void getSubjectByTitle_NotFound_ThrowsException() {
        when(subjectRepository.findByTitle("Physics")).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            subjectService.getSubjectByTitle("Physics", testUser);
        });
    }

    @Test
    void deleteSubject_Success() {
        when(subjectRepository.findById(10L)).thenReturn(Optional.of(testSubject));
        doNothing().when(subjectRepository).delete(testSubject);

        assertDoesNotThrow(() -> subjectService.deleteSubject(10L, testUser));
        verify(subjectRepository, times(1)).delete(testSubject);
    }
}
