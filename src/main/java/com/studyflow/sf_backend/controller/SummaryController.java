package com.studyflow.sf_backend.controller;

import com.studyflow.sf_backend.entity.Summary;
import com.studyflow.sf_backend.entity.Task;
import com.studyflow.sf_backend.service.SummaryService;
import com.studyflow.sf_backend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/summaries")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping
    public List<Summary> getAllSummaries() {
        return summaryService.getSummaries();
    }

    // 📌 Criar tarefa vinculada a uma matéria
    @PostMapping("/subject/{subjectId}")
    public Summary createSummary(@PathVariable Long subjectId, @RequestBody Summary summaryToCreate) {
        return summaryService.createSummary(subjectId, summaryToCreate);
    }

    // 📌 Atualizar tarefa
    @PutMapping("/{id}")
    public Summary updateSummary(@PathVariable Long id, @RequestBody Summary summaryToUpdate) {
        return summaryService.updateSummary(id, summaryToUpdate);
    }

    // 📌 Deletar tarefa
    @DeleteMapping("/{id}")
    public void deleteSummary(@PathVariable Long id) {
        summaryService.deleteSummary(id);
    }
}
