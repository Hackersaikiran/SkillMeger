package com.skillmerge.repositories;

import com.skillmerge.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	List<Project> findByRecruiterId(Long recruiterId);
}
