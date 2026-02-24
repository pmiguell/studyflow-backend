package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.dto.request.SummaryRequestDTO;
import com.studyflow.sf_backend.dto.response.SummaryResponseDTO;
import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.Summary;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final SummaryRepository summaryRepository;
    private final SubjectRepository subjectRepository;

    /**
     * Retrieves all summaries owned by the given user (through subject ownership).
     *
     * @param user the user whose summaries to retrieve
     * @return list of SummaryResponseDTO for all user summaries
     */
    public List<SummaryResponseDTO> getSummariesByUser(User user) {
        return summaryRepository.findBySubject_User(user)
                .stream()
                .map(SummaryResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new summary for a given subject, verifying user ownership of the subject.
     *
     * @param subjectId the id of the subject the summary belongs to
     * @param dto contains summary title and content
     * @param user the authenticated user creating the summary
     * @return SummaryResponseDTO for the created summary
     * @throws ResponseStatusException with HTTP 404 if subject not found or HTTP 403 if user does not own the subject
     */
    public SummaryResponseDTO createSummary(Long subjectId, SummaryRequestDTO dto, User user) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matéria não encontrada"));

        verifySubjectOwnership(subject, user);

        Summary newSummary = new Summary();
        newSummary.setTitle(dto.title());
        newSummary.setContent(dto.content());
        newSummary.setSubject(subject);

        Summary saved = summaryRepository.save(newSummary);
        return new SummaryResponseDTO(saved);
    }

    /**
     * Updates an existing summary, verifying user ownership of the summary's subject.
     *
     * @param id the id of the summary to update
     * @param dto contains new summary title and content
     * @param user the authenticated user requesting the update
     * @return SummaryResponseDTO for the updated summary
     * @throws ResponseStatusException with HTTP 404 if summary not found or HTTP 403 if user does not own the subject
     */
    public SummaryResponseDTO updateSummary(Long id, SummaryRequestDTO dto, User user) {
        Summary summaryToUpdate = getSummaryEntityById(id);

        verifySubjectOwnership(summaryToUpdate.getSubject(), user);

        summaryToUpdate.setTitle(dto.title());
        summaryToUpdate.setContent(dto.content());

        Summary saved = summaryRepository.save(summaryToUpdate);
        return new SummaryResponseDTO(saved);
    }

    /**
     * Deletes a summary, verifying user ownership of the summary's subject.
     *
     * @param id the id of the summary to delete
     * @param user the authenticated user requesting the deletion
     * @throws ResponseStatusException with HTTP 404 if summary not found or HTTP 403 if user does not own the subject
     */
    public void deleteSummary(Long id, User user) {
        Summary summary = getSummaryEntityById(id);

        verifySubjectOwnership(summary.getSubject(), user);

        summaryRepository.delete(summary);
    }

    private Summary getSummaryEntityById(Long id) {
        return summaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resumo não encontrado"));
    }

    private void verifySubjectOwnership(Subject subject, User user) {
        if (!subject.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar ou modificar este conteúdo");
        }
    }

}
