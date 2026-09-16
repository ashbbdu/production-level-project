package com.api_task_management.project.repository;


import com.api_task_management.project.dto.type.ProjectStatus;
import com.api_task_management.project.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    public boolean existsByName(String name);

//    public Page<ProjectEntity> findAll(Pageable pageable);
//    @Query("SELECT p FROM ProjectEntity p")
//    Page<ProjectEntity> getAllProjects(Pageable pageable , Sort sort);

    Page<ProjectEntity> findByStatus(ProjectStatus status, Pageable pageable);

    Page<ProjectEntity> findAllByNameAndStatus(String name , ProjectStatus status,Pageable pageable  );
}
