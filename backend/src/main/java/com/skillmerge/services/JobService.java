package com.skillmerge.services;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.skillmerge.dto.JobRequest;
import com.skillmerge.dto.PageResponse;
import com.skillmerge.entities.Job;
import com.skillmerge.entities.Recruiter;
import com.skillmerge.exceptions.ResourceNotFoundException;
import com.skillmerge.repositories.JobRepository;
import com.skillmerge.repositories.RecruiterRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;

    public JobService(JobRepository jobRepository, RecruiterRepository recruiterRepository) {
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public Job create(JobRequest request) {
        Recruiter recruiter = recruiterRepository.findById(request.getRecruiterId())
            .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        if (request.getSkillsRequired() != null) {
            job.setSkillsRequired(request.getSkillsRequired());
        }
        job.setExperienceRequired(request.getExperienceRequired());
        job.setRecruiter(recruiter);
        return jobRepository.save(job);
    }

    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    public Job getById(Long id) {
        return jobRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
    }

    public Job update(Long id, JobRequest request) {
        Job job = getById(id);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        if (request.getSkillsRequired() != null) {
            job.setSkillsRequired(request.getSkillsRequired());
        }
        job.setExperienceRequired(request.getExperienceRequired());
        return jobRepository.save(job);
    }

    public void delete(Long id) {
        jobRepository.deleteById(id);
    }

    public List<Job> getJobsByRecruiterId(Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
            .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        return jobRepository.findAll().stream()
            .filter(job -> job.getRecruiter().getId().equals(recruiterId))
            .collect(Collectors.toList());
    }

    public PageResponse<Job> search(String skill, Integer minExperience, int page, int size, String sortBy, String direction) {
        List<Job> jobs = jobRepository.findAll();
        if (skill != null && !skill.isBlank()) {
            String normalized = skill.toLowerCase(Locale.ROOT).trim();
            String[] tokens = normalized.split("\\s+");
            jobs = jobs.stream()
                .filter(job -> matchesAnyToken(job.getTitle(), tokens)
                    || matchesAnyToken(job.getDescription(), tokens)
                    || job.getSkillsRequired().stream()
                        .anyMatch(s -> matchesAnyToken(s, tokens)))
                .collect(Collectors.toList());
        }
        if (minExperience != null && minExperience > 0) {
            jobs = jobs.stream().filter(job -> job.getExperienceRequired() <= minExperience).collect(Collectors.toList());
        }

        Comparator<Job> comparator = Comparator.comparing(Job::getTitle);
        if ("experience".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Job::getExperienceRequired);
        }
        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }
        jobs = jobs.stream().sorted(comparator).collect(Collectors.toList());

        int total = jobs.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<Job> slice = jobs.subList(fromIndex, toIndex);
        int totalPages = (int) Math.ceil((double) total / size);

        return new PageResponse<>(slice, page, size, total, totalPages);
    }

    private boolean matchesAnyToken(String value, String[] tokens) {
        if (value == null || value.isBlank()) {
            return false;
        }
        String normalized = value.toLowerCase(Locale.ROOT);
        for (String token : tokens) {
            if (!token.isBlank() && normalized.contains(token)) {
                return true;
            }
        }
        return false;
    }
}
