package com.skillmerge.services;

import com.skillmerge.dto.NotificationResponse;
import com.skillmerge.dto.PageResponse;
import com.skillmerge.entities.Candidate;
import com.skillmerge.entities.Notification;
import com.skillmerge.exceptions.ResourceNotFoundException;
import com.skillmerge.repositories.CandidateRepository;
import com.skillmerge.repositories.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CandidateRepository candidateRepository;

    public NotificationService(NotificationRepository notificationRepository, CandidateRepository candidateRepository) {
        this.notificationRepository = notificationRepository;
        this.candidateRepository = candidateRepository;
    }

    public void createNotification(Long candidateId, String message) {
        Candidate candidate = candidateRepository.findById(candidateId)
            .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
        Notification notification = new Notification();
        notification.setCandidate(candidate);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    public PageResponse<NotificationResponse> getNotifications(Long candidateId, int page, int size) {
        Page<Notification> notifications = notificationRepository.findByCandidateIdOrderByCreatedAtDesc(
            candidateId, PageRequest.of(page, size));
        return new PageResponse<>(
            notifications.map(item -> new NotificationResponse(item.getId(), item.getMessage(), item.getCreatedAt(), item.isRead())).toList(),
            notifications.getNumber(),
            notifications.getSize(),
            notifications.getTotalElements(),
            notifications.getTotalPages()
        );
    }

    public void markRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
