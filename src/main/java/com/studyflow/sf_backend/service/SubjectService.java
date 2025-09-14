package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public List<Subject> getSubjectsByUser(Long userId) {
        return subjectRepository.findByUserId(userId);
    }

    public Subject getSubjectByTitle(String title) {
        return subjectRepository.findByTitle(title);
    }

    public Subject createSubject(Long userId, Subject subject) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        subject.setUser(user);
        subject.setTitle(subject.getTitle());
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long id, Subject updated) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + id));
        subject.setTitle(updated.getTitle());
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + id));
        subjectRepository.delete(subject);
    }

    public Subject getSubject(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + id));
    }

}
