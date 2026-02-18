package com.skillmerge.dto;

import jakarta.validation.constraints.NotNull;

public class ApplicationRequest {
    @NotNull
    private Long candidateId;

    @NotNull
    private String type;

    private Long jobId;

    private Long projectId;

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
