package com.skillmerge.services;

import com.skillmerge.dto.AnalyticsResponse;
import com.skillmerge.entities.Candidate;
import com.skillmerge.entities.Job;
import com.skillmerge.entities.Project;
import com.skillmerge.repositories.ApplicationRepository;
import com.skillmerge.repositories.CandidateRepository;
import com.skillmerge.repositories.JobRepository;
import com.skillmerge.repositories.ProjectRepository;
import com.skillmerge.repositories.RecruiterRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class AnalyticsService {

    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobRepository jobRepository;
    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;

    public AnalyticsService(CandidateRepository candidateRepository,
                            RecruiterRepository recruiterRepository,
                            JobRepository jobRepository,
                            ProjectRepository projectRepository,
                            ApplicationRepository applicationRepository) {
        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
        this.jobRepository = jobRepository;
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
    }

    public AnalyticsResponse getAnalytics() {
        int averageJobMatch = calculateAverageJobMatch();
        int averageProjectMatch = calculateAverageProjectMatch();
        return new AnalyticsResponse(
            candidateRepository.count(),
            recruiterRepository.count(),
            jobRepository.count(),
            projectRepository.count(),
            applicationRepository.count(),
            averageJobMatch,
            averageProjectMatch
        );
    }

    private int calculateAverageJobMatch() {
        List<Candidate> candidates = candidateRepository.findAll();
        List<Job> jobs = jobRepository.findAll();
        if (candidates.isEmpty() || jobs.isEmpty()) {
            return 0;
        }
        long total = 0;
        long count = 0;
        for (Candidate candidate : candidates) {
            for (Job job : jobs) {
                total += calculateMatch(candidate.getSkills(), candidate.getExperience(), job.getSkillsRequired(), job.getExperienceRequired());
                count++;
            }
        }
        return (int) Math.round((double) total / count);
    }

    private int calculateAverageProjectMatch() {
        List<Candidate> candidates = candidateRepository.findAll();
        List<Project> projects = projectRepository.findAll();
        if (candidates.isEmpty() || projects.isEmpty()) {
            return 0;
        }
        long total = 0;
        long count = 0;
        for (Candidate candidate : candidates) {
            for (Project project : projects) {
                total += calculateMatch(candidate.getSkills(), candidate.getExperience(), project.getSkillsRequired(), project.getExperienceRequired());
                count++;
            }
        }
        return (int) Math.round((double) total / count);
    }

    private int calculateMatch(List<String> candidateSkills, int candidateExperience, List<String> requiredSkills, int requiredExperience) {
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return 0;
        }
        List<String> normalizedCandidate = candidateSkills == null ? new ArrayList<>() : candidateSkills.stream()
            .map(skill -> skill.toLowerCase(Locale.ROOT).trim())
            .toList();

        long overlap = requiredSkills.stream()
            .map(skill -> skill.toLowerCase(Locale.ROOT).trim())
            .filter(normalizedCandidate::contains)
            .count();

        double skillScore = (double) overlap / requiredSkills.size();
        double experienceScore = requiredExperience == 0 ? 1.0 : Math.min((double) candidateExperience / requiredExperience, 1.0);

        int percent = (int) Math.round((skillScore * 0.7 + experienceScore * 0.3) * 100);
        return Math.min(100, Math.max(0, percent));
    }
}
