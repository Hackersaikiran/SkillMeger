package com.skillmerge.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.skillmerge.entities.AdminUser;
import com.skillmerge.entities.Candidate;
import com.skillmerge.entities.Job;
import com.skillmerge.entities.Project;
import com.skillmerge.entities.Recruiter;
import com.skillmerge.repositories.AdminUserRepository;
import com.skillmerge.repositories.CandidateRepository;
import com.skillmerge.repositories.JobRepository;
import com.skillmerge.repositories.ProjectRepository;
import com.skillmerge.repositories.RecruiterRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final AdminUserRepository adminUserRepository;
    private final JobRepository jobRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(CandidateRepository candidateRepository,
                      RecruiterRepository recruiterRepository,
                      AdminUserRepository adminUserRepository,
                      JobRepository jobRepository,
                      ProjectRepository projectRepository,
                      PasswordEncoder passwordEncoder) {
        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
        this.adminUserRepository = adminUserRepository;
        this.jobRepository = jobRepository;
        this.projectRepository = projectRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (candidateRepository.count() == 0) {
            Candidate candidate = new Candidate();
            candidate.setName("Alex Johnson");
            candidate.setEmail("alex@skillmerge.dev");
            candidate.setPassword(passwordEncoder.encode("password123"));
            candidate.setSkills(List.of("Java", "Spring", "SQL", "React"));
            candidate.setExperience(3);
            candidateRepository.save(candidate);
        }

        if (recruiterRepository.count() == 0) {
            Recruiter recruiter = new Recruiter();
            recruiter.setName("Jamie Recruiter");
            recruiter.setEmail("recruiter@skillmerge.dev");
            recruiter.setPassword(passwordEncoder.encode("password123"));
            recruiter.setCompany("SkillMerge Labs");
            recruiterRepository.save(recruiter);
        }

        if (adminUserRepository.count() == 0) {
            AdminUser admin = new AdminUser();
            admin.setName("Admin User");
            admin.setEmail("admin@skillmerge.dev");
            admin.setPassword(passwordEncoder.encode("admin123"));
            adminUserRepository.save(admin);
        }

        if (jobRepository.count() == 0) {
            Recruiter recruiter = recruiterRepository.findAll().get(0);
            Job job = new Job();
            job.setTitle("Backend Java Developer");
            job.setDescription("Build REST APIs with Spring Boot and PostgreSQL.");
            job.setSkillsRequired(List.of("Java", "Spring", "PostgreSQL"));
            job.setExperienceRequired(2);
            job.setRecruiter(recruiter);
            jobRepository.save(job);

            Job webJob = new Job();
            webJob.setTitle("Web Developer");
            webJob.setDescription("Build responsive web apps and landing pages.");
            webJob.setSkillsRequired(List.of("Web", "HTML", "CSS", "JavaScript"));
            webJob.setExperienceRequired(2);
            webJob.setRecruiter(recruiter);
            jobRepository.save(webJob);
        }

        if (projectRepository.count() == 0) {
            Recruiter recruiter = recruiterRepository.findAll().get(0);
            Project project = new Project();
            project.setTitle("React Dashboard Revamp");
            project.setDescription("Upgrade analytics dashboard UI with modern React.");
            project.setSkillsRequired(List.of("React", "CSS", "Chart.js"));
            project.setExperienceRequired(1);
            project.setRecruiter(recruiter);
            projectRepository.save(project);

            Project webProject = new Project();
            webProject.setTitle("Marketing Website Revamp");
            webProject.setDescription("Refresh a marketing site with modern UI.");
            webProject.setSkillsRequired(List.of("Web", "HTML", "CSS", "JavaScript"));
            webProject.setExperienceRequired(1);
            webProject.setRecruiter(recruiter);
            projectRepository.save(webProject);
        }
    }
}
