package com.api_task_management.project.repository;


import com.api_task_management.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    public boolean existsByName(String name);
}
