package com.api_task_management.project.service;

import com.api_task_management.common.exception.BusinessException;
import com.api_task_management.common.exception.ResourceAlreadyExistsException;
import com.api_task_management.project.dto.request.CreateProjectRequest;
import com.api_task_management.project.dto.response.ProjectResponse;
import com.api_task_management.project.dto.type.ProjectStatus;
import com.api_task_management.project.entity.ProjectEntity;
import com.api_task_management.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {

        if(projectRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Project with this name already exists ");
        }

        if(request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessException("End date can not be before start date !");
        }

        ProjectEntity project = new ProjectEntity();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setStatus(ProjectStatus.PLANNED); // not required here because we are setting this as PLANNED by default in the Entity

        ProjectEntity savedProject = projectRepository.save(project);

        ProjectResponse response = new ProjectResponse();
        response.setId(savedProject.getId());
        response.setName(savedProject.getName());
        response.setDescription(savedProject.getDescription());
        response.setStatus(savedProject.getStatus());
        response.setStartDate(savedProject.getStartDate());
        response.setEndDate(savedProject.getEndDate());
        response.setCreatedAt(savedProject.getCreatedAt());
        response.setUpdatedAt(savedProject.getUpdatedAt());

        return response;
    }
}
