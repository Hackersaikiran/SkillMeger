package com.skillmerge.controllers;

import com.skillmerge.dto.AnalyticsResponse;
import com.skillmerge.dto.ApplicationStatusUpdateRequest;
import com.skillmerge.entities.Candidate;
import com.skillmerge.entities.Application;
import com.skillmerge.entities.ApplicationStatus;
import com.skillmerge.entities.Job;
import com.skillmerge.entities.Project;
import com.skillmerge.entities.Recruiter;
import com.skillmerge.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final CandidateService candidateService;
    private final RecruiterService recruiterService;
    private final JobService jobService;
    private final ProjectService projectService;
    private final AnalyticsService analyticsService;
    private final ApplicationService applicationService;

    public AdminController(CandidateService candidateService,
                           RecruiterService recruiterService,
                           JobService jobService,
                           ProjectService projectService,
                           AnalyticsService analyticsService,
                           ApplicationService applicationService) {
        this.candidateService = candidateService;
        this.recruiterService = recruiterService;
        this.jobService = jobService;
        this.projectService = projectService;
        this.analyticsService = analyticsService;
        this.applicationService = applicationService;
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> getCandidates() {
        return ResponseEntity.ok(candidateService.getAll());
    }

    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recruiters")
    public ResponseEntity<List<Recruiter>> getRecruiters() {
        return ResponseEntity.ok(recruiterService.getAll());
    }

    @DeleteMapping("/recruiters/{id}")
    public ResponseEntity<Void> deleteRecruiter(@PathVariable Long id) {
        recruiterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getJobs() {
        return ResponseEntity.ok(jobService.getAll());
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects() {
        return ResponseEntity.ok(projectService.getAll());
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsResponse> analytics() {
        return ResponseEntity.ok(analyticsService.getAnalytics());
    }

    @PatchMapping("/applications/{applicationId}/status")
    public ResponseEntity<Application> updateApplicationStatus(@PathVariable Long applicationId,
                                                                @RequestBody ApplicationStatusUpdateRequest request) {
        ApplicationStatus status = ApplicationStatus.valueOf(request.getStatus().toUpperCase());
        return ResponseEntity.ok(applicationService.updateStatusForAdmin(applicationId, status));
    }
}
