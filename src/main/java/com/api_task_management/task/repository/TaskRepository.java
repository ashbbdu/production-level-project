package com.api_task_management.task.repository;

import com.api_task_management.task.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity , Long> {
    public Page<TaskEntity> findByProjectId(Long projectId , Pageable pageable);
}
