package com.skillmerge.controllers;

import com.skillmerge.dto.NotificationResponse;
import com.skillmerge.dto.PageResponse;
import com.skillmerge.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/candidate/{candidateId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<PageResponse<NotificationResponse>> getByCandidate(@PathVariable Long candidateId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(notificationService.getNotifications(candidateId, page, size));
    }

    @PostMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> markRead(@PathVariable Long notificationId) {
        notificationService.markRead(notificationId);
        return ResponseEntity.noContent().build();
    }
}
