package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.dto.request.TaskRequestDTO;
import com.studyflow.sf_backend.dto.response.TaskResponseDTO;
import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.Task;
import com.studyflow.sf_backend.entity.User;
import com.studyflow.sf_backend.enums.TaskStatus;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final SubjectRepository subjectRepository;

    /**
     * Retrieves all tasks owned by the given user (through subject ownership).
     *
     * @param user the user whose tasks to retrieve
     * @return list of TaskResponseDTO for all user tasks
     */
    public List<TaskResponseDTO> getTasksByUser(User user) {
        return taskRepository.findBySubject_User(user)
                .stream()
                .map(TaskResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new task for a given subject, verifying user ownership of the subject.
     *
     * @param subjectId the id of the subject the task belongs to
     * @param dto contains task title, description, deadline, completion status, and task status
     * @param user the authenticated user creating the task
     * @return TaskResponseDTO for the created task
     * @throws ResponseStatusException with HTTP 404 if subject not found or HTTP 403 if user does not own the subject
     */
    public TaskResponseDTO createTask(Long subjectId, TaskRequestDTO dto, User user) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matéria não encontrada"));

        verifySubjectOwnership(subject, user);

        Task newTask = new Task();
        newTask.setTitle(dto.title());
        newTask.setDescription(dto.description());
        newTask.setDeadline(dto.deadline());
        newTask.setCompleted(dto.completed());
        newTask.setSubject(subject);
        newTask.setStatus(dto.status() != null ? dto.status() : TaskStatus.NAO_INICIADO);

        Task saved = taskRepository.save(newTask);
        return new TaskResponseDTO(saved);
    }

    /**
     * Updates an existing task, verifying user ownership of the task's subject.
     *
     * @param id the id of the task to update
     * @param dto contains new task title, description, deadline, completion status, and task status
     * @param user the authenticated user requesting the update
     * @return TaskResponseDTO for the updated task
     * @throws ResponseStatusException with HTTP 404 if task not found or HTTP 403 if user does not own the subject
     */
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO dto, User user) {
        Task taskToUpdate = getTaskEntityById(id);

        verifySubjectOwnership(taskToUpdate.getSubject(), user);

        taskToUpdate.setTitle(dto.title());
        taskToUpdate.setDescription(dto.description());
        taskToUpdate.setCompleted(dto.completed());
        taskToUpdate.setDeadline(dto.deadline());
        taskToUpdate.setStatus(dto.status());

        Task saved = taskRepository.save(taskToUpdate);
        return new TaskResponseDTO(saved);
    }


    /**
     * Deletes a task, verifying user ownership of the task's subject.
     *
     * @param id the id of the task to delete
     * @param user the authenticated user requesting the deletion
     * @throws ResponseStatusException with HTTP 404 if task not found or HTTP 403 if user does not own the subject
     */
    public void deleteTask(Long id, User user) {
        Task task = getTaskEntityById(id);

        verifySubjectOwnership(task.getSubject(), user);

        taskRepository.delete(task);
    }

    private Task getTaskEntityById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    private void verifySubjectOwnership(Subject subject, User user) {
        if (!subject.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar ou modificar esta tarefa");
        }
    }

}
