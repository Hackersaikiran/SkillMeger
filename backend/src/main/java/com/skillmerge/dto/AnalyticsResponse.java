package com.skillmerge.dto;

public class AnalyticsResponse {
    private final long totalCandidates;
    private final long totalRecruiters;
    private final long totalJobs;
    private final long totalProjects;
    private final long totalApplications;
    private final int averageJobMatch;
    private final int averageProjectMatch;

    public AnalyticsResponse(long totalCandidates,
                             long totalRecruiters,
                             long totalJobs,
                             long totalProjects,
                             long totalApplications,
                             int averageJobMatch,
                             int averageProjectMatch) {
        this.totalCandidates = totalCandidates;
        this.totalRecruiters = totalRecruiters;
        this.totalJobs = totalJobs;
        this.totalProjects = totalProjects;
        this.totalApplications = totalApplications;
        this.averageJobMatch = averageJobMatch;
        this.averageProjectMatch = averageProjectMatch;
    }

    public long getTotalCandidates() {
        return totalCandidates;
    }

    public long getTotalRecruiters() {
        return totalRecruiters;
    }

    public long getTotalJobs() {
        return totalJobs;
    }

    public long getTotalProjects() {
        return totalProjects;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public int getAverageJobMatch() {
        return averageJobMatch;
    }

    public int getAverageProjectMatch() {
        return averageProjectMatch;
    }
}
