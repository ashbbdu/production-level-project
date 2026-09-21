package com.api_task_management.task.service;

import com.api_task_management.common.response.PageResponse;
import com.api_task_management.task.dto.request.CreateTaskRequest;
import com.api_task_management.task.dto.response.TaskResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    public TaskResponse createTask (CreateTaskRequest request , Long projectId);
    public TaskResponse getTaskById (Long taskId);
//    public PageResponse<TaskResponse> getTasksByProjectId(Long projectId , int page , int size);
    public PageResponse<TaskResponse> getTasksByProjectId(Long projectId , Pageable pageable);
}
