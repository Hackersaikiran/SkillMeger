package com.skillmerge.controllers;

import com.skillmerge.dto.PageResponse;
import com.skillmerge.dto.ProjectRequest;
import com.skillmerge.entities.Project;
import com.skillmerge.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> create(@Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<Project>> search(@RequestParam(required = false) String skill,
                                                        @RequestParam(required = false) Integer minExperience,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "6") int size,
                                                        @RequestParam(defaultValue = "title") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(projectService.search(skill, minExperience, page, size, sortBy, direction));
    }
}
