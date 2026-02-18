package com.skillmerge.dto;

import java.util.List;

public class MatchingItem {
    private final Long id;
    private final String type;
    private final List<String> skillsRequired;
    private final int experienceRequired;
    private final String title;

    public MatchingItem(Long id, String type, List<String> skillsRequired, int experienceRequired, String title) {
        this.id = id;
        this.type = type;
        this.skillsRequired = skillsRequired;
        this.experienceRequired = experienceRequired;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<String> getSkillsRequired() {
        return skillsRequired;
    }

    public int getExperienceRequired() {
        return experienceRequired;
    }

    public String getTitle() {
        return title;
    }
}
