package com.skillmerge.services;

import com.skillmerge.entities.Candidate;
import com.skillmerge.exceptions.ResourceNotFoundException;
import com.skillmerge.repositories.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<Candidate> getAll() {
        return candidateRepository.findAll();
    }

    public Candidate getById(Long id) {
        return candidateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
    }

    public Candidate update(Long id, Candidate updated) {
        Candidate candidate = getById(id);
        candidate.setName(updated.getName());
        candidate.setSkills(updated.getSkills());
        candidate.setExperience(updated.getExperience());
        candidate.setResumeUrl(updated.getResumeUrl());
        return candidateRepository.save(candidate);
    }

    public void delete(Long id) {
        candidateRepository.deleteById(id);
    }
}
