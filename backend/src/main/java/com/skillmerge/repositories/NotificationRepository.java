package com.skillmerge.repositories;

import com.skillmerge.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByCandidateIdOrderByCreatedAtDesc(Long candidateId, Pageable pageable);
}
