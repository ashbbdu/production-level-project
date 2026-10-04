package com.api_task_management.task.service;

import com.api_task_management.common.response.PageResponse;
import com.api_task_management.task.dto.request.CreateTaskRequest;
import com.api_task_management.task.dto.request.TaskFilterRequest;
import com.api_task_management.task.dto.response.TaskListResponse;
import com.api_task_management.task.dto.response.TaskResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskService {
    public TaskResponse createTask (CreateTaskRequest request , Long projectId);
    public TaskResponse getTaskById (Long taskId);
//    public PageResponse<TaskResponse> getTasksByProjectId(Long projectId , int page , int size);
//    public PageResponse<TaskResponse> getTasksByProjectId(Long projectId , Pageable pageable , TaskFilterRequest filter);
    public void testNPlusOne();

    public PageResponse<TaskListResponse> getTasksByProjectId(Long projectId , Pageable pageable , TaskFilterRequest filter);
    public PageResponse<TaskResponse> getTasksUsingEntityGraph( Long projectId, Pageable pageable , TaskFilterRequest filter);

    public PageResponse<TaskResponse> testBatchFetching( Long projectId, Pageable pageable , TaskFilterRequest filter);
}

