package com.api_task_management.project.service;

import com.api_task_management.common.exception.BusinessException;
import com.api_task_management.common.exception.ResourceAlreadyExistsException;
import com.api_task_management.common.exception.ResourceNotFoundException;
import com.api_task_management.common.response.PageResponse;
import com.api_task_management.project.constant.ProjectSortFields;
import com.api_task_management.project.dto.request.CreateProjectRequest;
import com.api_task_management.project.dto.request.ProjectFilterRequest;
import com.api_task_management.project.dto.request.UpdateProjectRequest;
import com.api_task_management.project.dto.response.ProjectResponse;
import com.api_task_management.project.dto.type.ProjectStatus;
import com.api_task_management.project.entity.ProjectEntity;
import com.api_task_management.project.repository.ProjectRepository;
import com.api_task_management.project.repository.ProjectSpecification;
import com.api_task_management.task.dto.request.TaskRequestProject;
import com.api_task_management.task.entity.TaskEntity;
import com.api_task_management.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;




    private ProjectResponse toProjectResponse(ProjectEntity project) {
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
        response.setTasks(project.getTasks().stream().map(this::toProjectTaskResponse).toList());

        return response;
    }

    private TaskRequestProject toProjectTaskResponse(TaskEntity task) {
        TaskRequestProject request = new TaskRequestProject();
        request.setId(task.getId());
        request.setTitle(task.getTitle());
        request.setDescription(task.getDescription());
        request.setStatus(task.getStatus());
        request.setPriority(task.getPriority());
        request.setDueDate(task.getDueDate());
        request.setCreatedAt(task.getCreatedAt());
        request.setUpdatedAt(task.getUpdatedAt());

        return request;

    }


    private void validateSort(Pageable pageable) {

        for (Sort.Order order : pageable.getSort()) {

            if (!ProjectSortFields.ALLOWED_FIELDS.contains(order.getProperty())) {
                throw new BusinessException(
                        "Sorting by '" + order.getProperty() + "' is not allowed"
                );
            }
        }
    }

    private void validateStatusTransition(
            ProjectStatus currentStatus,
            ProjectStatus newStatus) {

        if (currentStatus == newStatus) {
            return;
        }

        boolean validTransition = switch (currentStatus) {
            case PLANNED -> newStatus == ProjectStatus.ACTIVE;
            case ACTIVE -> newStatus == ProjectStatus.COMPLETED;
            case COMPLETED -> newStatus == ProjectStatus.ARCHIVED;
            case ARCHIVED -> false;
        };

        if (!validTransition) {
            throw new BusinessException(
                    "Invalid project status transition from "
                            + currentStatus
                            + " to "
                            + newStatus
            );
        }
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
public PageResponse<ProjectResponse> getAllProjects (ProjectFilterRequest filter , Pageable pageable) {
//    Sort sort = Sort.by("name").ascending();
//    Pageable pageable = PageRequest.of(page,size , Sort.by("name").descending());


    if(filter.getStartDateFrom() != null
            && filter.getStartDateTo() != null
            && filter.getStartDateFrom().isAfter(filter.getStartDateTo())) {
        throw new BusinessException("startDateFrom cannot be after startDateTo");
    }

    validateSort(pageable);

    if (pageable.getPageSize() > 100) {
        throw new BusinessException(
                "Page size cannot exceed 100"
        );
    }


    Specification<ProjectEntity> specification =
            ProjectSpecification.filter(filter);

    Page<ProjectEntity> projects = projectRepository.findAll(specification ,pageable);

//    if(status != null && name != null) {
//        System.out.println("1");
//        projects = projectRepository.findAllByNameAndStatus(name, status ,pageable);
//    } else if (status != null) {
//        projects = projectRepository.findByStatus(status ,pageable);
//    } else {
//        System.out.println("3");
//        projects  = projectRepository.findAll(pageable);
//    }

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

    @Override
    @Transactional
    public ProjectResponse updateProject(
            Long projectId,
            UpdateProjectRequest request) {

        ProjectEntity project =
                projectRepository.findById(projectId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Project with id : "
                                                + projectId
                                                + " does not exist!"
                                )
                        );
        if (request.getName() != null) {

            if (!request.getName().equals(project.getName())
                    && projectRepository.existsByName(
                    request.getName())) {

                throw new ResourceAlreadyExistsException(
                        "Project with this name already exists!"
                );
            }
        }


        LocalDateTime newStartDate =
                request.getStartDate() != null
                        ? request.getStartDate()
                        : project.getStartDate();

        LocalDateTime newEndDate =
                request.getEndDate() != null
                        ? request.getEndDate()
                        : project.getEndDate();

        if (newEndDate != null
                && newEndDate.isBefore(newStartDate)) {

            throw new BusinessException(
                    "End date cannot be before start date!"
            );
        }


        if (request.getName() != null) {
            project.setName(request.getName());
        }

        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }

        if (request.getStartDate() != null) {
            project.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            project.setEndDate(request.getEndDate());
        }

        if (request.getStatus() != null) {

            validateStatusTransition(
                    project.getStatus(),
                    request.getStatus()
            );

            project.setStatus(request.getStatus());
        }

        return toResponse(project);
    }



    private void validatePagination(Pageable pageable) {

        if (pageable.getPageSize() > 100) {
            throw new BusinessException(
                    "Page size cannot exceed 100"
            );
        }
    }


//    working/learning on pagination , sorting ,




    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProjectResponse> testProjectTaskFetch(Pageable pageable) {

        Page<ProjectEntity> projects =  projectRepository.findProjectsWithTasks(pageable);

        List<ProjectResponse> projectList = projects.stream().map(this::toResponse).toList();

        PageResponse<ProjectResponse> response = new PageResponse<>(
                projectList,
                projects.getNumber(),
                projects.getSize(),
                projects.getTotalElements(),
                projects.getTotalPages(),
                projects.hasNext(),
                projects.hasPrevious()
        );

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProjectResponse> testProjectTaskFetchTwoQuery(
            ProjectFilterRequest filter,
            Pageable pageable
    ) {

//    add startdate can not be after enddate logic and validateSort logic
//    also add filters here

//        Specification<ProjectEntity> specification =
//                ProjectSpecification.filter(filter);

        Page<ProjectEntity> projects =
                projectRepository.findAll(ProjectSpecification.filter(filter) , pageable);

        // We will implement Query 2 here next.

        List<Long> projectIds = projects.getContent()
                .stream().map(ProjectEntity::getId).toList();

//        if the projects are 0 in the requested page

        List<TaskEntity> tasks = projectIds.isEmpty()
                ? List.of()
                : taskRepository.findByProjectIdIn(projectIds);

//        List<TaskEntity> tasks = taskRepository.findByProjectIdIn(projectIds);



        Map<Long, List<TaskEntity>> tasksByProject =
                tasks.stream()
                        .collect(Collectors.groupingBy(
                                task -> task.getProject().getId()
                        ));

//        Build ProjectResponse
        List<ProjectResponse> projectList =
                projects.getContent()
                        .stream()
                        .map(project -> {

                            ProjectResponse response =
//                                    toResponse(project);
                                    toProjectResponse(project);

                            List<TaskRequestProject> projectTasks =
                                    tasksByProject.getOrDefault(
                                                    project.getId(),
                                                    List.of()
                                            )
                                            .stream()
                                            .map(this::toProjectTaskResponse)
                                            .toList();

                            response.setTasks(projectTasks);

                            return response;
                        })
                        .toList();

        return new PageResponse<>(
                projectList,
                projects.getNumber(),
                projects.getSize(),
                projects.getTotalElements(),
                projects.getTotalPages(),
                projects.hasNext(),
                projects.hasPrevious()
        );
    }


//    using Entity Graph with 2 query approach

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProjectResponse> testProjectEntityGraph(Pageable pageable) {

        Page<ProjectEntity> projects =  projectRepository.findProjectsWithTasksUsingEntityGraph(pageable);

        List<ProjectResponse> projectList = projects.stream().map(this::toResponse).toList();

        PageResponse<ProjectResponse> response = new PageResponse<>(
                projectList,
                projects.getNumber(),
                projects.getSize(),
                projects.getTotalElements(),
                projects.getTotalPages(),
                projects.hasNext(),
                projects.hasPrevious()
        );

        return response;
    }


}
