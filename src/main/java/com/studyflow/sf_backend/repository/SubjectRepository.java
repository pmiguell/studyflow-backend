package com.studyflow.sf_backend.repository;

import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByUserId(Long userId);
    List<Subject> findByUser(User user);
    Subject findByTitle(String title);
}
