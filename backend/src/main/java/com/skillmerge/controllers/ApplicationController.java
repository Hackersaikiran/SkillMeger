package com.skillmerge.controllers;

import com.skillmerge.dto.ApplicationRequest;
import com.skillmerge.dto.ApplicationStatusUpdateRequest;
import com.skillmerge.dto.PageResponse;
import com.skillmerge.entities.Application;
import com.skillmerge.entities.ApplicationStatus;
import com.skillmerge.exceptions.BadRequestException;
import com.skillmerge.services.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Application> apply(@Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.apply(request));
    }

    @GetMapping("/candidate/{candidateId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<Application>> getByCandidate(@PathVariable Long candidateId) {
        return ResponseEntity.ok(applicationService.getByCandidate(candidateId));
    }

    @GetMapping("/candidate/{candidateId}/page")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<PageResponse<Application>> getByCandidatePaged(@PathVariable Long candidateId,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(applicationService.getByCandidate(candidateId, page, size));
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Application> updateStatus(@PathVariable Long applicationId,
                                                    @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        if (request.getRecruiterId() == null) {
            throw new BadRequestException("recruiterId is required");
        }
        ApplicationStatus status = ApplicationStatus.valueOf(request.getStatus().toUpperCase());
        return ResponseEntity.ok(applicationService.updateStatusForRecruiter(applicationId, status, request.getRecruiterId()));
    }
}
