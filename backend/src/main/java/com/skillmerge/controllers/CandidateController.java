package com.skillmerge.controllers;

import com.skillmerge.entities.Candidate;
import com.skillmerge.services.CandidateService;
import com.skillmerge.services.StorageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;
    private final StorageService storageService;

    public CandidateController(CandidateService candidateService, StorageService storageService) {
        this.candidateService = candidateService;
        this.storageService = storageService;
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> getAll() {
        return ResponseEntity.ok(candidateService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getById(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidate> update(@PathVariable Long id, @Valid @RequestBody Candidate candidate) {
        return ResponseEntity.ok(candidateService.update(id, candidate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        candidateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<Map<String, String>> uploadResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String url = storageService.store(file);
        Candidate candidate = candidateService.getById(id);
        candidate.setResumeUrl(url);
        candidateService.update(id, candidate);
        return ResponseEntity.ok(Map.of("resumeUrl", url));
    }
}
