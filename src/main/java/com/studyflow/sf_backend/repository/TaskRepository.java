package com.studyflow.sf_backend.repository;

import com.studyflow.sf_backend.entity.Task;
import com.studyflow.sf_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findBySubject_User(User user);

}
