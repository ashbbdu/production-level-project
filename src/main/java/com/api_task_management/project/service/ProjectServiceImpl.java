package com.api_task_management.project.service;

import com.api_task_management.common.exception.BusinessException;
import com.api_task_management.common.exception.ResourceAlreadyExistsException;
import com.api_task_management.common.exception.ResourceNotFoundException;
import com.api_task_management.common.response.PageResponse;
import com.api_task_management.project.dto.request.CreateProjectRequest;
import com.api_task_management.project.dto.response.ProjectResponse;
import com.api_task_management.project.dto.type.ProjectStatus;
import com.api_task_management.project.entity.ProjectEntity;
import com.api_task_management.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;
    private ProjectResponse toResponse(ProjectEntity project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStatus(project.getStatus());
        response.setStartDate(project.getStartDate());
        response.setEndDate(project.getEndDate());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        return response;
    }



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




        return toResponse(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id " + projectId + " not found !"));

        return toResponse(project);
    }

//    @Override
//    public Page<ProjectResponse> getAllProjects() {
//        Pageable pageable = PageRequest.of(0,2);
//        Page<ProjectEntity> projects = projectRepository.findAll(pageable);
////       return  projectRepository.findAll(pageable);
//
////        ProjectResponse pr = new ProjectResponse();
////
////
////        return projects.map(this::toResponse);
//
////        List<ProjectResponse> responses = projects.getContent().stream().map(res -> toResponse(res)).toList();
////
//        List<ProjectResponse> response = projects.getContent().stream().map(project -> {
//            ProjectResponse resp = new ProjectResponse();
//
//            resp.setId(project.getId());
//            resp.setName(project.getName());
//            resp.setDescription(project.getDescription());
//            resp.setStatus(project.getStatus());
//            resp.setStartDate(project.getStartDate());
//            resp.setEndDate(project.getEndDate());
//            resp.setCreatedAt(project.getCreatedAt());
//            resp.setUpdatedAt(project.getUpdatedAt());
//
//            return resp;
//
//        }).toList();
////
////        return List.of(pr);
//        return new PageImpl<>(
//                response,
//                projects.getPageable(),
//                projects.getTotalElements()
//        );
//    }


@Override
public PageResponse<ProjectResponse> getAllProjects (String name , ProjectStatus status , Pageable pageable) {
//    Sort sort = Sort.by("name").ascending();
//    Pageable pageable = PageRequest.of(page,size , Sort.by("name").descending());
    Page<ProjectEntity> projects;

    if(status != null && name != null) {
        System.out.println("1");
        projects = projectRepository.findAllByNameAndStatus(name, status ,pageable);
    } else if (status != null) {
        projects = projectRepository.findByStatus(status ,pageable);
    } else {
        System.out.println("3");
        projects  = projectRepository.findAll(pageable);
    }

    List<ProjectResponse> response = projects.getContent().stream().map(project -> {
        ProjectResponse resp = new ProjectResponse();

        resp.setId(project.getId());
        resp.setName(project.getName());
        resp.setDescription(project.getDescription());
        resp.setStatus(project.getStatus());
        resp.setStartDate(project.getStartDate());
        resp.setEndDate(project.getEndDate());
        resp.setCreatedAt(project.getCreatedAt());
        resp.setUpdatedAt(project.getUpdatedAt());

        return resp;

    }).toList();

//    return new PageImpl<>(
//            response,
//            projects.getPageable(),
//            projects.getTotalElements()
//    );

    return new PageResponse<>(
            response,
            projects.getNumber(),
            projects.getSize(),
            projects.getTotalElements(),
            projects.getTotalPages(),
            projects.hasNext(),
            projects.hasPrevious()
    );
}


}
