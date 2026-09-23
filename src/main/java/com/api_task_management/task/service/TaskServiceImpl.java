package com.api_task_management.task.service;

import com.api_task_management.common.advice.ApiResponse;
import com.api_task_management.common.exception.BusinessException;
import com.api_task_management.common.exception.ResourceNotFoundException;
import com.api_task_management.common.response.PageResponse;
import com.api_task_management.project.entity.ProjectEntity;
import com.api_task_management.project.repository.ProjectRepository;
import com.api_task_management.project.repository.ProjectSpecification;
import com.api_task_management.task.constant.TaskSortFields;
import com.api_task_management.task.dto.request.CreateTaskRequest;
import com.api_task_management.task.dto.request.TaskFilterRequest;
import com.api_task_management.task.dto.response.TaskResponse;
import com.api_task_management.task.entity.TaskEntity;
import com.api_task_management.task.repository.TaskRepository;
import com.api_task_management.task.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private void validateSort (Pageable pageable) {
        for(Sort.Order s : pageable.getSort()) {
            if(!TaskSortFields.ALLOWED_FIELDS.contains(s.getProperty())) {
                throw new BusinessException("Sorting by '" + s.getProperty() + "' is not allowed");
            }
        }
    }

    public TaskResponse toResponse (TaskEntity task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
//        response.setProjectId(task.getProject().getId());

        return response;
    }

    @Transactional
    @Override
    public TaskResponse createTask(CreateTaskRequest request, Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new ResourceNotFoundException("Project with id : " + projectId + " not found"));

        TaskEntity task = new TaskEntity();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setProject(project);

        TaskEntity savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long taskId) {

        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() ->
                new ResourceNotFoundException("Task with id : " + taskId + " not found"));

        return toResponse(task);
    }

//    @Override
//    public PageResponse<TaskResponse> getTasksByProjectId(Long projectId) {
//        ProjectEntity projects = projectRepository.findById(projectId).orElseThrow(() ->
//                new ResourceNotFoundException("Not found")
//                );
//
//        Pageable pageable = PageRequest.of(0, 10);
//        List<TaskEntity> tasks = projects.getTasks();
//
//        Page<TaskEntity> taskPage = new PageImpl<>(
//                tasks,
//                pageable,
//                tasks.size()
//        );
//
//
//        List<TaskResponse> response = new ArrayList<>();
//        for (var t : tasks) {
//            response.add(toResponse(t));
//        }
//
//        PageResponse<TaskResponse> pr = new PageResponse<>(
//                response,
//                taskPage.getNumber(),
//                taskPage.getSize(),
//                taskPage.getTotalElements(),
//                taskPage.getTotalPages(),
//                taskPage.hasNext(),
//                taskPage.hasPrevious()
//
//
//        );
//        return pr;
//    }

    @Override
    public PageResponse<TaskResponse> getTasksByProjectId(Long projectId , Pageable pageable , TaskFilterRequest filter) {

        if (pageable.getPageSize() > 100) {
            throw new BusinessException(
                    "Page size cannot exceed 100"
            );
        }

        validateSort(pageable);

        ProjectEntity projects = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Not found")
        );






//        Pageable pageable = PageRequest.of(page, size);
//        List<TaskEntity> tasks = projects.getTasks();

//        now to support specification we need to pass specification instead of projectId;
//        Page<TaskEntity> tasks = taskRepository.findByProjectId(projectId , pageable);
        Specification<TaskEntity> specification =
                TaskSpecification.filter(projectId , filter);
//        Page<TaskEntity> tasks = taskRepository.findByProjectId(specification , pageable);

//        not using findByProjectId because it does not support specification
        Page<TaskEntity> tasks = taskRepository.findAll(specification , pageable);
//
//        Page<TaskEntity> taskPage = new PageImpl<>(
//                tasks,
//                pageable,
//                tasks.size()
//        );

//        List<TaskEntity> tasks1 = taskRepository.findAll();

//        List<TaskEntity> tasks1 = taskRepository.findAllTasks();
//
//        for (TaskEntity task : tasks1) {
//            System.out.println(task.getProject().getName() + " : " + task.getTitle()) ;
//        }


        List<TaskResponse> response = new ArrayList<>();
        for (var t : tasks) {
            System.out.println(t.getProject().getName() + " project Namemmemme");
            response.add(toResponse(t));
        }

//        PageResponse<TaskResponse> pr = new PageResponse<>(
//                response,
//                tasks.getNumber(),
//                tasks.getSize(),
//                tasks.getTotalElements(),
//                tasks.getTotalPages(),
//                tasks.hasNext(),
//                tasks.hasPrevious()
//
//
//        );
        return new PageResponse<>(
                response,
                tasks.getNumber(),
                tasks.getSize(),
                tasks.getTotalElements(),
                tasks.getTotalPages(),
                tasks.hasNext(),
                tasks.hasPrevious()


        );
    }

    @Transactional(readOnly = true)
    public void testNPlusOne() {

        List<TaskEntity> tasks = taskRepository.findAllWithProject();

        for (TaskEntity task : tasks) {
            System.out.println(
                    task.getTitle() + " -> " +
                            task.getProject().getName()
            );
        }
    }
}
