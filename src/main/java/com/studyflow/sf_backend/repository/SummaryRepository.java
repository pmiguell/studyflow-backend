package com.studyflow.sf_backend.repository;

import com.studyflow.sf_backend.entity.Summary;
import com.studyflow.sf_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SummaryRepository extends JpaRepository<Summary, Long> {
    List<Summary> findBySubject_User(User user);

}
