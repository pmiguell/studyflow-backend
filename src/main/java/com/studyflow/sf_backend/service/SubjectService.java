package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.dto.request.SubjectRequestDTO;
import com.studyflow.sf_backend.dto.response.SubjectResponseDTO;
import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves all subjects owned by the given user.
     *
     * @param user the user whose subjects to retrieve
     * @return list of SubjectResponseDTO for all user subjects
     */
    public List<SubjectResponseDTO> getSubjectsByUser(User user) {
        return subjectRepository.findByUser(user)
                .stream()
                .map(SubjectResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a subject by title, verifying user ownership.
     *
     * @param title the title of the subject to search
     * @param user the authenticated user requesting the subject
     * @return SubjectResponseDTO for the found subject
     * @throws ResponseStatusException with HTTP 404 if subject not found or HTTP 403 if user does not own it
     */
    public SubjectResponseDTO getSubjectByTitle(String title, User user) {
        Subject subject = subjectRepository.findByTitle(title);

        if (subject == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Matéria não encontrada");
        }

        verifySubjectOwnership(subject, user);

        return new SubjectResponseDTO(subject);
    }

    /**
     * Creates a new subject for the given user.
     *
     * @param user the user creating the subject
     * @param dto contains the subject title
     * @return SubjectResponseDTO for the created subject
     */
    public SubjectResponseDTO createSubject(User user, SubjectRequestDTO dto) {
        Subject subject = new Subject();
        subject.setTitle(dto.title());
        subject.setColor(dto.color() != null ? dto.color() : "#8ed1fc");
        subject.setUser(user);

        Subject saved = subjectRepository.save(subject);
        return new SubjectResponseDTO(saved);
    }

    /**
     * Updates an existing subject, verifying user ownership.
     *
     * @param id the id of the subject to update
     * @param dto contains the new subject title
     * @param user the authenticated user requesting the update
     * @return SubjectResponseDTO for the updated subject
     * @throws ResponseStatusException with HTTP 404 if subject not found or HTTP 403 if user does not own it
     */
    public SubjectResponseDTO updateSubject(Long id, SubjectRequestDTO dto, User user) {
        Subject subject = getSubjectEntityById(id);

        verifySubjectOwnership(subject, user);

        subject.setTitle(dto.title());
        if (dto.color() != null) {
            subject.setColor(dto.color());
        }
        Subject saved = subjectRepository.save(subject);

        return new SubjectResponseDTO(saved);
    }

    /**
     * Deletes a subject and all associated tasks and summaries, verifying user ownership.
     *
     * @param id the id of the subject to delete
     * @param user the authenticated user requesting the deletion
     * @throws ResponseStatusException with HTTP 404 if subject not found or HTTP 403 if user does not own it
     */
    public void deleteSubject(Long id, User user) {
        Subject subject = getSubjectEntityById(id);

        verifySubjectOwnership(subject, user);

        subjectRepository.delete(subject);
    }

    private Subject getSubjectEntityById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matéria não encontrada"));
    }

    private void verifySubjectOwnership(Subject subject, User user) {
        if (!subject.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta matéria");
        }
    }

}
