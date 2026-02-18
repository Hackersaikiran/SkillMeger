package com.skillmerge.dto;

public class RecruiterAnalyticsResponse {
    private final long totalJobs;
    private final long totalProjects;
    private final long totalJobApplications;
    private final long totalProjectApplications;

    public RecruiterAnalyticsResponse(long totalJobs, long totalProjects, long totalJobApplications, long totalProjectApplications) {
        this.totalJobs = totalJobs;
        this.totalProjects = totalProjects;
        this.totalJobApplications = totalJobApplications;
        this.totalProjectApplications = totalProjectApplications;
    }

    public long getTotalJobs() {
        return totalJobs;
    }

    public long getTotalProjects() {
        return totalProjects;
    }

    public long getTotalJobApplications() {
        return totalJobApplications;
    }

    public long getTotalProjectApplications() {
        return totalProjectApplications;
    }
}
