package com.skillmerge.dto;

import java.util.List;

public class MatchingServiceRequest {
    private final List<String> skills;
    private final int experience;
    private final List<MatchingItem> items;

    public MatchingServiceRequest(List<String> skills, int experience, List<MatchingItem> items) {
        this.skills = skills;
        this.experience = experience;
        this.items = items;
    }

    public List<String> getSkills() {
        return skills;
    }

    public int getExperience() {
        return experience;
    }

    public List<MatchingItem> getItems() {
        return items;
    }
}
