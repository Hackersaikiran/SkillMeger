package com.skillmerge.repositories;

import com.skillmerge.entities.Application;
import com.skillmerge.entities.ApplicationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCandidateId(Long candidateId);
    Page<Application> findByCandidateId(Long candidateId, Pageable pageable);
    List<Application> findByTypeAndJobId(ApplicationType type, Long jobId);
    List<Application> findByTypeAndProjectId(ApplicationType type, Long projectId);
    long countByTypeAndJobId(ApplicationType type, Long jobId);
    long countByTypeAndProjectId(ApplicationType type, Long projectId);
}
