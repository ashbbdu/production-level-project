package com.api_task_management.task.dto.request;

import com.api_task_management.task.dto.type.TaskPriority;
import com.api_task_management.task.dto.type.TaskStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateTaskRequest {
    @Size(min = 2 , max = 200 , message = "Title should be in between 2 and 200 characters")
    private String title;
    @Size(max = 2000 , message = "Description should max 2000 characters")
    private String description;
    private LocalDateTime dueDate;


    private TaskStatus status;
    private TaskPriority priority;
}
