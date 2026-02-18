package com.skillmerge.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillmerge.dto.RecruiterAnalyticsResponse;
import com.skillmerge.entities.Application;
import com.skillmerge.entities.Job;
import com.skillmerge.entities.Recruiter;
import com.skillmerge.services.ApplicationService;
import com.skillmerge.services.JobService;
import com.skillmerge.services.RecruiterAnalyticsService;
import com.skillmerge.services.RecruiterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    private final RecruiterService recruiterService;
    private final ApplicationService applicationService;
    private final RecruiterAnalyticsService recruiterAnalyticsService;
    private final JobService jobService;

    public RecruiterController(RecruiterService recruiterService,
                               ApplicationService applicationService,
                               RecruiterAnalyticsService recruiterAnalyticsService,
                               JobService jobService) {
        this.recruiterService = recruiterService;
        this.applicationService = applicationService;
        this.recruiterAnalyticsService = recruiterAnalyticsService;
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<Recruiter>> getAll() {
        return ResponseEntity.ok(recruiterService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recruiter> update(@PathVariable Long id, @Valid @RequestBody Recruiter recruiter) {
        return ResponseEntity.ok(recruiterService.update(id, recruiter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recruiterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs/{jobId}/applicants")
    public ResponseEntity<List<Application>> getJobApplicants(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getJobApplicants(jobId));
    }

    @GetMapping("/projects/{projectId}/applicants")
    public ResponseEntity<List<Application>> getProjectApplicants(@PathVariable Long projectId) {
        return ResponseEntity.ok(applicationService.getProjectApplicants(projectId));
    }

    @GetMapping("/{recruiterId}/analytics")
    public ResponseEntity<RecruiterAnalyticsResponse> getAnalytics(@PathVariable Long recruiterId) {
        return ResponseEntity.ok(recruiterAnalyticsService.getAnalytics(recruiterId));
    }

    @GetMapping("/{recruiterId}/jobs")
    public ResponseEntity<List<Job>> getRecruiterJobs(@PathVariable Long recruiterId) {
        return ResponseEntity.ok(jobService.getJobsByRecruiterId(recruiterId));
    }
}
