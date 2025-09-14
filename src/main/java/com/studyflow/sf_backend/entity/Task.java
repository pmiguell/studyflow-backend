package com.studyflow.sf_backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "tasks", "user"})
    private Subject subject;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    private boolean completed = false;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.NAO_INICIADO;

    public enum TaskStatus {
        NAO_INICIADO,
        EM_ANDAMENTO,
        CONCLUIDO
    }
}
