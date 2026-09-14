package com.api_task_management.project.controller;

import com.api_task_management.common.advice.ApiResponse;
import com.api_task_management.project.dto.request.CreateProjectRequest;
import com.api_task_management.project.dto.response.ProjectResponse;
import com.api_task_management.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject (@RequestBody @Valid CreateProjectRequest request) {
        ProjectResponse response = projectService.createProject(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();


        ApiResponse<ProjectResponse> apiResponse = new ApiResponse<>(
                true,
                "Project created successfully !",
                response
                );
       return ResponseEntity.created(location).body(apiResponse);
//        return projectService.createProject(request);
    }

    @GetMapping(path = "/{projectId}")
    public ApiResponse<ProjectResponse> getProjectById (@PathVariable Long projectId) {

        ProjectResponse response = projectService.getProjectById(projectId);
        return new ApiResponse<>(
                true,
                "Project fetched successfully !",
                response
        );
    }
}
