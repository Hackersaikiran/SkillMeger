package com.skillmerge.dto;

public class MatchResult {
    private final Long id;
    private final String type;
    private final String title;
    private final int matchPercent;

    public MatchResult(Long id, String type, String title, int matchPercent) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.matchPercent = matchPercent;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getMatchPercent() {
        return matchPercent;
    }
}
