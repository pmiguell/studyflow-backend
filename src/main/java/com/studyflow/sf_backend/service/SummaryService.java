package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.Summary;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final SummaryRepository summaryRepository;
    private final SubjectRepository subjectRepository;

    public List<Summary> getSummaries() {
        return summaryRepository.findAll();
    }

    public Summary createSummary(Long subjectId, Summary summary) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + subjectId));

        Summary newSummary = new Summary();
        newSummary.setTitle(summary.getTitle());
        newSummary.setContent(summary.getContent());
        newSummary.setSubject(subject);

        return summaryRepository.save(newSummary);
    }

    public Summary updateSummary(Long id, Summary newSummary) {
        Summary summaryToUpdate = summaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Summary not found: " + id));

        summaryToUpdate.setTitle(newSummary.getTitle());
        summaryToUpdate.setContent(newSummary.getContent());

        return summaryRepository.save(summaryToUpdate);
    }


    public void deleteSummary(Long id) {
        Summary summary = summaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Summary not found: " + id));
        summaryRepository.delete(summary);
    }
}
