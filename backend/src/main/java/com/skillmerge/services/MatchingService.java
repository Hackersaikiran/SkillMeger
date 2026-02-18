package com.skillmerge.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.skillmerge.dto.MatchResult;
import com.skillmerge.dto.MatchingItem;
import com.skillmerge.dto.MatchingRequest;
import com.skillmerge.dto.MatchingServiceRequest;
import com.skillmerge.dto.MatchingServiceResponse;
import com.skillmerge.dto.MatchingServiceResult;
import com.skillmerge.entities.Candidate;
import com.skillmerge.entities.Job;
import com.skillmerge.entities.Project;
import com.skillmerge.exceptions.ResourceNotFoundException;
import com.skillmerge.repositories.CandidateRepository;
import com.skillmerge.repositories.JobRepository;
import com.skillmerge.repositories.ProjectRepository;

@Service
public class MatchingService {

    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final ProjectRepository projectRepository;
    private final RestTemplate restTemplate;

    @Value("${app.matching.service-url}")
    private String matchingServiceUrl;

    @Value("${app.matching.use-remote}")
    private boolean useRemote;

    public MatchingService(CandidateRepository candidateRepository,
                           JobRepository jobRepository,
                           ProjectRepository projectRepository,
                           RestTemplate restTemplate) {
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.projectRepository = projectRepository;
        this.restTemplate = restTemplate;
    }

    public List<MatchResult> recommend(MatchingRequest request) {
        Candidate candidate = candidateRepository.findById(request.getCandidateId())
            .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        List<String> skills = request.getSkills() != null ? request.getSkills() : candidate.getSkills();
        Integer requestedExperience = request.getExperience();
        int experience = requestedExperience != null ? requestedExperience : candidate.getExperience();

        List<Job> jobs = jobRepository.findAll();
        List<Project> projects = projectRepository.findAll();

        if (useRemote) {
            return remoteMatch(skills, experience, jobs, projects);
        }

        List<MatchResult> results = new ArrayList<>();
        for (Job job : jobs) {
            int score = calculateMatch(skills, experience, job.getSkillsRequired(), job.getExperienceRequired());
            results.add(new MatchResult(job.getId(), "JOB", job.getTitle(), score));
        }
        for (Project project : projects) {
            int score = calculateMatch(skills, experience, project.getSkillsRequired(), project.getExperienceRequired());
            results.add(new MatchResult(project.getId(), "PROJECT", project.getTitle(), score));
        }
        return results.stream()
            .sorted((a, b) -> Integer.compare(b.getMatchPercent(), a.getMatchPercent()))
            .collect(Collectors.toList());
    }

    private List<MatchResult> remoteMatch(List<String> skills, int experience, List<Job> jobs, List<Project> projects) {
        List<MatchingItem> items = new ArrayList<>();
        for (Job job : jobs) {
            items.add(new MatchingItem(job.getId(), "JOB", job.getSkillsRequired(), job.getExperienceRequired(), job.getTitle()));
        }
        for (Project project : projects) {
            items.add(new MatchingItem(project.getId(), "PROJECT", project.getSkillsRequired(), project.getExperienceRequired(), project.getTitle()));
        }

        MatchingServiceRequest payload = new MatchingServiceRequest(skills, experience, items);
        MatchingServiceResponse response = restTemplate.postForObject(matchingServiceUrl, payload, MatchingServiceResponse.class);

        Map<Long, Integer> scores = new HashMap<>();
        if (response != null && response.getResults() != null) {
            for (MatchingServiceResult result : response.getResults()) {
                scores.put(result.getId(), result.getMatchPercent());
            }
        }

        List<MatchResult> results = new ArrayList<>();
        for (MatchingItem item : items) {
            int score = scores.getOrDefault(item.getId(), 0);
            results.add(new MatchResult(item.getId(), item.getType(), item.getTitle(), score));
        }
        return results.stream()
            .sorted((a, b) -> Integer.compare(b.getMatchPercent(), a.getMatchPercent()))
            .collect(Collectors.toList());
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
        // Weight skills higher than experience to reflect core capability fit.
        double experienceScore = requiredExperience == 0 ? 1.0 : Math.min((double) candidateExperience / requiredExperience, 1.0);

        int percent = (int) Math.round((skillScore * 0.7 + experienceScore * 0.3) * 100);
        return Math.min(100, Math.max(0, percent));
    }
}
