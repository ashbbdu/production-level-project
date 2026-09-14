package com.api_task_management.project.service;

import com.api_task_management.project.dto.request.CreateProjectRequest;
import com.api_task_management.project.dto.response.ProjectResponse;

public interface ProjectService {
    public ProjectResponse createProject (CreateProjectRequest request);
}
