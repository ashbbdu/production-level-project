package com.api_task_management.task.controller;

import com.api_task_management.common.advice.ApiResponse;
import com.api_task_management.common.response.PageResponse;
import com.api_task_management.task.dto.request.CreateTaskRequest;
import com.api_task_management.task.dto.request.TaskFilterRequest;
import com.api_task_management.task.dto.response.TaskResponse;
import com.api_task_management.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping(path = "/{projectId}/tasks")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask (@RequestBody @Valid CreateTaskRequest request, @PathVariable Long projectId) {
        TaskResponse task = taskService.createTask(request, projectId);
        ApiResponse<TaskResponse> response = new ApiResponse<>(
                true,
                "Task Created Successfully !",
                task
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(task.getId())
                .toUri();


        return ResponseEntity.created(location).body(response);
    }

    @GetMapping(path = "/tasks/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById (@PathVariable Long taskId) {
        TaskResponse task = taskService.getTaskById(taskId);
        ApiResponse<TaskResponse> response = new ApiResponse<>(
                true,
                "Task Fetched Successfully !",
                task
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/projects/{projectId}/tasks")
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getTasksByProjectId
            (@PathVariable Long projectId ,
//             @RequestParam(defaultValue = "0") int page,
//             @RequestParam(defaultValue = "10") int size
             @PageableDefault(page = 0 , size = 10 , sort = "createdAt" , direction = Sort.Direction.ASC) Pageable pageable,
             @ModelAttribute TaskFilterRequest filter // adding this for filter functionality
            ) {

//        PageResponse<TaskResponse> tasks = taskService.getTasksByProjectId(projectId , pageable.getPageNumber() , pageable.getPageSize());
        PageResponse<TaskResponse> tasks = taskService.getTasksByProjectId(projectId , pageable , filter);


//        System.out.println(filter.getPriority() + " " + filter.getStatus());
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Tasks Fetched Successfully !",
                tasks
        ));
    }


    @GetMapping(path = "/testNPlusOne")
    public void testNPlusOne() {
        taskService.testNPlusOne();
    }

}
