package com.skillmerge.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class MatchingRequest {
    @NotNull
    private Long candidateId;

    private List<String> skills;

    private Integer experience;

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }
}
