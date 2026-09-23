package com.api_task_management.task.repository;

import com.api_task_management.task.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity , Long> , JpaSpecificationExecutor<TaskEntity> {
//    public Page<TaskEntity> findByProjectId(Long projectId , Pageable pageable);
public Page<TaskEntity> findByProjectId(Specification<TaskEntity> spec, Pageable pageable);

    @Query("""
    SELECT t
    FROM TaskEntity t
    JOIN FETCH t.project
""")
    List<TaskEntity> findAllWithProject();
}
