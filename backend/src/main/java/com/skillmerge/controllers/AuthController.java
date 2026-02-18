package com.skillmerge.controllers;

import com.skillmerge.dto.*;
import com.skillmerge.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/candidate")
    public ResponseEntity<AuthResponse> registerCandidate(@Valid @RequestBody CandidateRegisterRequest request) {
        return ResponseEntity.ok(authService.registerCandidate(request));
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<AuthResponse> registerRecruiter(@Valid @RequestBody RecruiterRegisterRequest request) {
        return ResponseEntity.ok(authService.registerRecruiter(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
