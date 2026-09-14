package com.api_task_management.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateProjectRequest {

    @NotBlank(message = "Project name is required!")
    @Size(
            min = 2,
            max = 100,
            message = "Project name must be between 2 and 100 characters"
    )
    private String name;

    private String description;

    @NotNull(message = "Start date is required !")
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
