package com.skillmerge.dto;

import java.util.List;

public class MatchingServiceResponse {
    private List<MatchingServiceResult> results;

    public List<MatchingServiceResult> getResults() {
        return results;
    }

    public void setResults(List<MatchingServiceResult> results) {
        this.results = results;
    }
}
