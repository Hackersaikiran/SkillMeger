package com.skillmerge.security;

import com.skillmerge.entities.AdminUser;
import com.skillmerge.entities.Candidate;
import com.skillmerge.entities.Recruiter;
import com.skillmerge.entities.Role;
import com.skillmerge.repositories.AdminUserRepository;
import com.skillmerge.repositories.CandidateRepository;
import com.skillmerge.repositories.RecruiterRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final AdminUserRepository adminUserRepository;

    public CustomUserDetailsService(CandidateRepository candidateRepository,
                                    RecruiterRepository recruiterRepository,
                                    AdminUserRepository adminUserRepository) {
        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("Use loadUserByEmailAndRole instead");
    }

    public UserPrincipal loadUserByEmailAndRole(String email, Role role) {
        if (role == Role.CANDIDATE) {
            Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
            return new UserPrincipal(candidate.getId(), candidate.getEmail(), candidate.getPassword(), role.name());
        }
        if (role == Role.RECRUITER) {
            Recruiter recruiter = recruiterRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Recruiter not found"));
            return new UserPrincipal(recruiter.getId(), recruiter.getEmail(), recruiter.getPassword(), role.name());
        }
        AdminUser admin = adminUserRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        return new UserPrincipal(admin.getId(), admin.getEmail(), admin.getPassword(), role.name());
    }
}
