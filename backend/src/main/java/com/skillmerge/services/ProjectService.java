package com.skillmerge.services;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.skillmerge.dto.PageResponse;
import com.skillmerge.dto.ProjectRequest;
import com.skillmerge.entities.Project;
import com.skillmerge.entities.Recruiter;
import com.skillmerge.exceptions.ResourceNotFoundException;
import com.skillmerge.repositories.ProjectRepository;
import com.skillmerge.repositories.RecruiterRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final RecruiterRepository recruiterRepository;

    public ProjectService(ProjectRepository projectRepository, RecruiterRepository recruiterRepository) {
        this.projectRepository = projectRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public Project create(ProjectRequest request) {
        Recruiter recruiter = recruiterRepository.findById(request.getRecruiterId())
            .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        if (request.getSkillsRequired() != null) {
            project.setSkillsRequired(request.getSkillsRequired());
        }
        project.setExperienceRequired(request.getExperienceRequired());
        project.setRecruiter(recruiter);
        return projectRepository.save(project);
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public Project getById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    public Project update(Long id, ProjectRequest request) {
        Project project = getById(id);
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        if (request.getSkillsRequired() != null) {
            project.setSkillsRequired(request.getSkillsRequired());
        }
        project.setExperienceRequired(request.getExperienceRequired());
        return projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public PageResponse<Project> search(String skill, Integer minExperience, int page, int size, String sortBy, String direction) {
        List<Project> projects = projectRepository.findAll();
        if (skill != null && !skill.isBlank()) {
            String normalized = skill.toLowerCase(Locale.ROOT).trim();
            String[] tokens = normalized.split("\\s+");
            projects = projects.stream()
                .filter(project -> matchesAnyToken(project.getTitle(), tokens)
                    || matchesAnyToken(project.getDescription(), tokens)
                    || project.getSkillsRequired().stream()
                        .anyMatch(s -> matchesAnyToken(s, tokens)))
                .collect(Collectors.toList());
        }
        if (minExperience != null && minExperience > 0) {
            projects = projects.stream().filter(project -> project.getExperienceRequired() <= minExperience).collect(Collectors.toList());
        }

        Comparator<Project> comparator = Comparator.comparing(Project::getTitle);
        if ("experience".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Project::getExperienceRequired);
        }
        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }
        projects = projects.stream().sorted(comparator).collect(Collectors.toList());

        int total = projects.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<Project> slice = projects.subList(fromIndex, toIndex);
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
