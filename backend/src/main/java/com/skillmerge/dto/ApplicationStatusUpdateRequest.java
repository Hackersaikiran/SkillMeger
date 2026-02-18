package com.skillmerge.dto;

import jakarta.validation.constraints.NotBlank;

public class ApplicationStatusUpdateRequest {
    @NotBlank
    private String status;

    private Long recruiterId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }
}
