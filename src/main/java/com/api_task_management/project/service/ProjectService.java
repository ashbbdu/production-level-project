package com.api_task_management.project.service;

import com.api_task_management.common.response.PageResponse;
import com.api_task_management.project.dto.request.CreateProjectRequest;
import com.api_task_management.project.dto.request.ProjectFilterRequest;
import com.api_task_management.project.dto.request.UpdateProjectRequest;
import com.api_task_management.project.dto.response.ProjectResponse;
import com.api_task_management.project.dto.type.ProjectStatus;
import com.api_task_management.project.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    public ProjectResponse createProject (CreateProjectRequest request);
    public ProjectResponse getProjectById(Long projectId);
//  public List<ProjectResponse> getAllProjects();
//    public PageResponse<ProjectResponse> getAllProjects(String name , ProjectStatus status , Pageable pageable) ;
public PageResponse<ProjectResponse> getAllProjects(ProjectFilterRequest filter , Pageable pageable) ;
public ProjectResponse updateProject (Long projectId , UpdateProjectRequest request);
}
