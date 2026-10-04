package com.api_task_management.task.repository;

import com.api_task_management.task.dto.request.TaskFilterRequest;
import com.api_task_management.task.dto.response.TaskListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskQueryRepository {
    Page<TaskListResponse> findTaskList(Long projectId, TaskFilterRequest filter, Pageable pageable);
}
