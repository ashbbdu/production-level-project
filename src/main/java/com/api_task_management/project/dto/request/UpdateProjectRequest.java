package com.api_task_management.project.dto.request;

import com.api_task_management.project.dto.type.ProjectStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateProjectRequest {
    @Size(
            min = 2,
            max = 100,
            message = "Project name must be between 2 and 100 characters"
    )
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectStatus status;
}
