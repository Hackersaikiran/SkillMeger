package com.skillmerge.services;

import com.skillmerge.dto.RecruiterAnalyticsResponse;
import com.skillmerge.entities.Job;
import com.skillmerge.entities.Project;
import com.skillmerge.repositories.ApplicationRepository;
import com.skillmerge.repositories.JobRepository;
import com.skillmerge.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterAnalyticsService {

    private final JobRepository jobRepository;
    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;

    public RecruiterAnalyticsService(JobRepository jobRepository,
                                     ProjectRepository projectRepository,
                                     ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
    }

    public RecruiterAnalyticsResponse getAnalytics(Long recruiterId) {
        List<Job> jobs = jobRepository.findByRecruiterId(recruiterId);
        List<Project> projects = projectRepository.findByRecruiterId(recruiterId);

        long jobApplications = 0;
        for (Job job : jobs) {
            jobApplications += applicationRepository.countByTypeAndJobId(com.skillmerge.entities.ApplicationType.JOB, job.getId());
        }

        long projectApplications = 0;
        for (Project project : projects) {
            projectApplications += applicationRepository.countByTypeAndProjectId(com.skillmerge.entities.ApplicationType.PROJECT, project.getId());
        }

        return new RecruiterAnalyticsResponse(jobs.size(), projects.size(), jobApplications, projectApplications);
    }
}
