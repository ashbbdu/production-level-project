package com.api_task_management.project.controller;

import com.api_task_management.common.advice.ApiResponse;
import com.api_task_management.common.response.PageResponse;
import com.api_task_management.project.dto.request.CreateProjectRequest;
import com.api_task_management.project.dto.request.ProjectFilterRequest;
import com.api_task_management.project.dto.request.UpdateProjectRequest;
import com.api_task_management.project.dto.response.ProjectResponse;
import com.api_task_management.project.dto.type.ProjectStatus;
import com.api_task_management.project.entity.ProjectEntity;
import com.api_task_management.project.repository.ProjectRepository;
import com.api_task_management.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.Getter;
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
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;

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

    @GetMapping
    public ApiResponse<PageResponse<ProjectResponse>> getAllProjects(
//                @RequestParam(defaultValue = "0")  int page
//            , @RequestParam(defaultValue = "10") int size)  instead of passing this getAllProjects(page , size) we will pass Pageable

            @ModelAttribute ProjectFilterRequest filter,
            @PageableDefault(size = 10 , page = 0 , sort = {"createdAt" , "id"} , direction = Sort.Direction.ASC) Pageable page
//            @RequestParam(required = false) ProjectStatus status,
//            @RequestParam(required = false) String name

            )
    {
        PageResponse<ProjectResponse> projects = projectService.getAllProjects(filter, page);
        return new ApiResponse<>(
                true,
                "tes",
                projects
        );
    }

    @PatchMapping(path = "/{projectId}")
    public ApiResponse<ProjectResponse> updateProject (@PathVariable Long projectId ,
                                                                     @RequestBody @Valid UpdateProjectRequest request) {
        ProjectResponse updatedProject = projectService.updateProject(projectId , request);

//        ApiResponse<ProjectResponse> response = new ApiResponse<>(true , "Project Updated Successfully !" ,updatedProject);
        return new ApiResponse<>(true , "Project Updated Successfully !" ,updatedProject);
    }






    @GetMapping("/learning/project-tasks")
    public ResponseEntity<ApiResponse<PageResponse<ProjectResponse>>> testProjectTaskFetch(
            @PageableDefault(size = 2) Pageable pageable
    ) {

        PageResponse<ProjectResponse> projects = projectService.testProjectTaskFetch(pageable);



        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Projects fetched testProjectTaskFetch",
                projects
        ));
    }

    //    this one is using 2 query solution

    @GetMapping("/learning/project-tasks-two-query")
    public ResponseEntity<ApiResponse<PageResponse<ProjectResponse>>>
    testProjectTaskFetchTwoQuery(
            @ModelAttribute ProjectFilterRequest filter,
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        PageResponse<ProjectResponse> projects =
                projectService.testProjectTaskFetchTwoQuery(
                        filter,
                        pageable
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Projects fetched using two-query approach",
                        projects
                )
        );
    }

//    entity graph with 2 query approach


    @GetMapping("/learning/project-tasks-two-query-entity-graph")
    public ResponseEntity<ApiResponse<PageResponse<ProjectResponse>>>
    testProjectEntityGraph(
            @ModelAttribute ProjectFilterRequest filter,
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        PageResponse<ProjectResponse> projects =
                projectService.testProjectEntityGraph(
                        pageable
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Projects fetched using two-query approach",
                        projects
                )
        );
    }

}
