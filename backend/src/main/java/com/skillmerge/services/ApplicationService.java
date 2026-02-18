package com.skillmerge.services;

import com.skillmerge.dto.ApplicationRequest;
import com.skillmerge.dto.PageResponse;
import com.skillmerge.entities.*;
import com.skillmerge.exceptions.BadRequestException;
import com.skillmerge.exceptions.ResourceNotFoundException;
import com.skillmerge.repositories.ApplicationRepository;
import com.skillmerge.repositories.CandidateRepository;
import com.skillmerge.repositories.JobRepository;
import com.skillmerge.repositories.ProjectRepository;
import com.skillmerge.repositories.RecruiterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final ProjectRepository projectRepository;
    private final RecruiterRepository recruiterRepository;
    private final NotificationService notificationService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              CandidateRepository candidateRepository,
                              JobRepository jobRepository,
                              ProjectRepository projectRepository,
                              RecruiterRepository recruiterRepository,
                              NotificationService notificationService) {
        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.projectRepository = projectRepository;
        this.recruiterRepository = recruiterRepository;
        this.notificationService = notificationService;
    }

    public Application apply(ApplicationRequest request) {
        Candidate candidate = candidateRepository.findById(request.getCandidateId())
            .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
        Application application = new Application();
        application.setCandidate(candidate);
        ApplicationType type = ApplicationType.valueOf(request.getType().toUpperCase());
        application.setType(type);

        if (type == ApplicationType.JOB) {
            if (request.getJobId() == null) {
                throw new BadRequestException("jobId is required for JOB application");
            }
            Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
            application.setJob(job);
        } else {
            if (request.getProjectId() == null) {
                throw new BadRequestException("projectId is required for PROJECT application");
            }
            Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
            application.setProject(project);
        }

        Application saved = applicationRepository.save(application);
        notificationService.createNotification(candidate.getId(), "Your application was submitted.");
        return saved;
    }

    public List<Application> getByCandidate(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    public PageResponse<Application> getByCandidate(Long candidateId, int page, int size) {
        Page<Application> applications = applicationRepository.findByCandidateId(candidateId, PageRequest.of(page, size));
        return new PageResponse<>(
            applications.getContent(),
            applications.getNumber(),
            applications.getSize(),
            applications.getTotalElements(),
            applications.getTotalPages()
        );
    }

    public List<Application> getJobApplicants(Long jobId) {
        return applicationRepository.findByTypeAndJobId(ApplicationType.JOB, jobId);
    }

    public List<Application> getProjectApplicants(Long projectId) {
        return applicationRepository.findByTypeAndProjectId(ApplicationType.PROJECT, projectId);
    }

    public Application updateStatusForRecruiter(Long applicationId, ApplicationStatus status, Long recruiterId) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        validateRecruiterOwnership(application, recruiterId);
        application.setStatus(status);
        Application saved = applicationRepository.save(application);
        notificationService.createNotification(application.getCandidate().getId(),
            "Your application status changed to " + status.name() + ".");
        return saved;
    }

    public Application updateStatusForAdmin(Long applicationId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        application.setStatus(status);
        Application saved = applicationRepository.save(application);
        notificationService.createNotification(application.getCandidate().getId(),
            "Your application status changed to " + status.name() + ".");
        return saved;
    }

    private void validateRecruiterOwnership(Application application, Long recruiterId) {
        recruiterRepository.findById(recruiterId)
            .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        if (application.getType() == ApplicationType.JOB) {
            if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
                throw new BadRequestException("Recruiter does not own this job");
            }
            return;
        }
        if (!application.getProject().getRecruiter().getId().equals(recruiterId)) {
            throw new BadRequestException("Recruiter does not own this project");
        }
    }
}
