package com.skillmerge.controllers;

import com.skillmerge.dto.MatchResult;
import com.skillmerge.dto.MatchingRequest;
import com.skillmerge.services.MatchingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @PostMapping("/recommendations")
    public ResponseEntity<List<MatchResult>> recommend(@Valid @RequestBody MatchingRequest request) {
        return ResponseEntity.ok(matchingService.recommend(request));
    }
}
