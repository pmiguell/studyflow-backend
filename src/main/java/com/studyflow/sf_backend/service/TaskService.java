package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.entity.Task;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final SubjectRepository subjectRepository;

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Long subjectId, Task task) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + subjectId));

        Task newTask = new Task();
        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setDeadline(task.getDeadline());
        newTask.setCompleted(task.isCompleted());
        newTask.setSubject(subject);

        return taskRepository.save(newTask);
    }

    public Task updateTask(Long id, Task newTask) {
        Task taskToUpdate = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));

        taskToUpdate.setTitle(newTask.getTitle());
        taskToUpdate.setDescription(newTask.getDescription());
        taskToUpdate.setCompleted(newTask.isCompleted());
        taskToUpdate.setDeadline(newTask.getDeadline());
        taskToUpdate.setStatus(newTask.getStatus()); // 🔹 Faltava isso!

        return taskRepository.save(taskToUpdate);
    }


    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
        taskRepository.delete(task);
    }
}
