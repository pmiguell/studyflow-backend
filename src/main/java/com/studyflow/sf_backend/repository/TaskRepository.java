package com.studyflow.sf_backend.repository;

import com.studyflow.sf_backend.entity.Task;
import com.studyflow.sf_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t LEFT JOIN t.subject s WHERE t.user = :user OR s.user = :user")
    List<Task> findAllByUserIncludingSubjectTasks(@Param("user") User user);
    
}
