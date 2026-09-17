package com.api_task_management.project.dto.request;

import com.api_task_management.project.dto.type.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectFilterRequest {
    private ProjectStatus status;

    private String name;

    private LocalDateTime startDateFrom;

    private LocalDateTime startDateTo;
}
