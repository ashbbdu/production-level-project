package com.api_task_management.project.dto.response;

import com.api_task_management.project.dto.type.ProjectStatus;
import com.api_task_management.task.dto.request.TaskRequestProject;
import com.api_task_management.task.dto.response.TaskResponse;
import com.api_task_management.task.entity.TaskEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TaskRequestProject> tasks;
}
