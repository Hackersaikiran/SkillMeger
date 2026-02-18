package com.skillmerge.dto;

public class MatchingServiceResult {
    private Long id;
    private int matchPercent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMatchPercent() {
        return matchPercent;
    }

    public void setMatchPercent(int matchPercent) {
        this.matchPercent = matchPercent;
    }
}
