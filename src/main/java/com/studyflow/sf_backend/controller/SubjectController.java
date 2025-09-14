package com.studyflow.sf_backend.controller;

import com.studyflow.sf_backend.entity.Subject;
import com.studyflow.sf_backend.repository.SubjectRepository;
import com.studyflow.sf_backend.repository.UserRepository;
import com.studyflow.sf_backend.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.studyflow.sf_backend.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject,
                                                 @AuthenticationPrincipal User loggedUser) {
        // Associa automaticamente o usuário logado
        subject.setUser(loggedUser);

        // Salva no banco
        Subject saved = subjectRepository.save(subject);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Subject> getSubjects() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // se você usa email como username
        User loggedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return subjectRepository.findByUser(loggedUser);
    }

    @GetMapping("/search")
    public ResponseEntity<Subject> getSubjectByTitle(@RequestParam String title,
                                                     @AuthenticationPrincipal User loggedUser) {
        Subject subject = subjectRepository.findByTitle(title);

        if (subject == null) {
            return ResponseEntity.notFound().build();
        }

        if (!subject.getUser().getId().equals(loggedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(subject);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id,
                                                 @RequestBody Subject updatedSubject,
                                                 @AuthenticationPrincipal User loggedUser) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));

        if (!subject.getUser().getId().equals(loggedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        subject.setTitle(updatedSubject.getTitle());
        Subject saved = subjectRepository.save(subject);

        // força o carregamento das tasks
        saved.getTasks().size();

        return ResponseEntity.ok(saved);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id,
                                              @AuthenticationPrincipal User loggedUser) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));

        if (!subject.getUser().getId().equals(loggedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        subjectRepository.delete(subject);
        return ResponseEntity.noContent().build();
    }
}
