package com.skillmerge.services;

import com.skillmerge.entities.Recruiter;
import com.skillmerge.exceptions.ResourceNotFoundException;
import com.skillmerge.repositories.RecruiterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;

    public RecruiterService(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    public List<Recruiter> getAll() {
        return recruiterRepository.findAll();
    }

    public Recruiter getById(Long id) {
        return recruiterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
    }

    public Recruiter update(Long id, Recruiter updated) {
        Recruiter recruiter = getById(id);
        recruiter.setName(updated.getName());
        recruiter.setCompany(updated.getCompany());
        return recruiterRepository.save(recruiter);
    }

    public void delete(Long id) {
        recruiterRepository.deleteById(id);
    }
}
