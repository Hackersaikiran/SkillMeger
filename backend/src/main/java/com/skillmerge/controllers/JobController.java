package com.skillmerge.controllers;

import com.skillmerge.dto.JobRequest;
import com.skillmerge.dto.PageResponse;
import com.skillmerge.entities.Job;
import com.skillmerge.services.JobService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<Job> create(@Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAll() {
        return ResponseEntity.ok(jobService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> update(@PathVariable Long id, @Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<Job>> search(@RequestParam(required = false) String skill,
                                                    @RequestParam(required = false) Integer minExperience,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "6") int size,
                                                    @RequestParam(defaultValue = "title") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(jobService.search(skill, minExperience, page, size, sortBy, direction));
    }
}
