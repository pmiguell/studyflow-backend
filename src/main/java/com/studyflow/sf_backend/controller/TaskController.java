package com.studyflow.sf_backend.controller;

import com.studyflow.sf_backend.entity.Task;
import com.studyflow.sf_backend.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getTasks();
    }

    // 📌 Criar tarefa vinculada a uma matéria
    @PostMapping("/subject/{subjectId}")
    public Task createTask(@PathVariable Long subjectId, @RequestBody Task taskToCreate) {
        return taskService.createTask(subjectId, taskToCreate);
    }

    // 📌 Atualizar tarefa
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task taskToUpdate) {
        return taskService.updateTask(id, taskToUpdate);
    }

    // 📌 Deletar tarefa
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
