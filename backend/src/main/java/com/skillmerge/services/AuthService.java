package com.skillmerge.services;

import com.skillmerge.dto.*;
import com.skillmerge.entities.AdminUser;
import com.skillmerge.entities.Candidate;
import com.skillmerge.entities.Recruiter;
import com.skillmerge.entities.Role;
import com.skillmerge.exceptions.BadRequestException;
import com.skillmerge.repositories.AdminUserRepository;
import com.skillmerge.repositories.CandidateRepository;
import com.skillmerge.repositories.RecruiterRepository;
import com.skillmerge.security.JwtTokenProvider;
import com.skillmerge.security.UserPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(CandidateRepository candidateRepository,
                       RecruiterRepository recruiterRepository,
                       AdminUserRepository adminUserRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponse registerCandidate(CandidateRegisterRequest request) {
        if (candidateRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Candidate email already exists");
        }
        Candidate candidate = new Candidate();
        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());
        candidate.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getSkills() != null) {
            candidate.setSkills(request.getSkills());
        }
        candidate.setExperience(request.getExperience());
        Candidate saved = candidateRepository.save(candidate);
        UserPrincipal principal = new UserPrincipal(saved.getId(), saved.getEmail(), saved.getPassword(), Role.CANDIDATE.name());
        return new AuthResponse(tokenProvider.generateToken(principal), Role.CANDIDATE.name(), saved.getId());
    }

    public AuthResponse registerRecruiter(RecruiterRegisterRequest request) {
        if (recruiterRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Recruiter email already exists");
        }
        Recruiter recruiter = new Recruiter();
        recruiter.setName(request.getName());
        recruiter.setEmail(request.getEmail());
        recruiter.setPassword(passwordEncoder.encode(request.getPassword()));
        recruiter.setCompany(request.getCompany());
        Recruiter saved = recruiterRepository.save(recruiter);
        UserPrincipal principal = new UserPrincipal(saved.getId(), saved.getEmail(), saved.getPassword(), Role.RECRUITER.name());
        return new AuthResponse(tokenProvider.generateToken(principal), Role.RECRUITER.name(), saved.getId());
    }

    public AuthResponse registerAdmin(AdminRegisterRequest request) {
        if (adminUserRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Admin email already exists");
        }
        AdminUser admin = new AdminUser();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        AdminUser saved = adminUserRepository.save(admin);
        UserPrincipal principal = new UserPrincipal(saved.getId(), saved.getEmail(), saved.getPassword(), Role.ADMIN.name());
        return new AuthResponse(tokenProvider.generateToken(principal), Role.ADMIN.name(), saved.getId());
    }

    public AuthResponse login(LoginRequest request) {
        Role role = Role.valueOf(request.getRole().toUpperCase());
        if (role == Role.CANDIDATE) {
            Candidate candidate = candidateRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
            if (!passwordEncoder.matches(request.getPassword(), candidate.getPassword())) {
                throw new BadRequestException("Invalid credentials");
            }
            UserPrincipal principal = new UserPrincipal(candidate.getId(), candidate.getEmail(), candidate.getPassword(), role.name());
            return new AuthResponse(tokenProvider.generateToken(principal), role.name(), candidate.getId());
        }
        if (role == Role.RECRUITER) {
            Recruiter recruiter = recruiterRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
            if (!passwordEncoder.matches(request.getPassword(), recruiter.getPassword())) {
                throw new BadRequestException("Invalid credentials");
            }
            UserPrincipal principal = new UserPrincipal(recruiter.getId(), recruiter.getEmail(), recruiter.getPassword(), role.name());
            return new AuthResponse(tokenProvider.generateToken(principal), role.name(), recruiter.getId());
        }
        AdminUser admin = adminUserRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        UserPrincipal principal = new UserPrincipal(admin.getId(), admin.getEmail(), admin.getPassword(), role.name());
        return new AuthResponse(tokenProvider.generateToken(principal), role.name(), admin.getId());
    }
}
