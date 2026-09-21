package com.api_task_management.task.repository;

import com.api_task_management.task.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity , Long> {
}
