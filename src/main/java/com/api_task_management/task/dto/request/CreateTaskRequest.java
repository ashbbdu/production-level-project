package com.api_task_management.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskRequest {
    @NotBlank(message = "Title is a required field !")
    @Size(min = 2 , max = 200 , message = "Title should be in between 2 and 200 characters")
    private String title;

    @Size(max = 2000 , message = "Description should max 200 characters")
    private String description;

    private LocalDateTime dueDate;
}
