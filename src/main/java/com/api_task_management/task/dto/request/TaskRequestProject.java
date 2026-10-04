package com.api_task_management.task.dto.request;

import com.api_task_management.task.dto.type.TaskPriority;
import com.api_task_management.task.dto.type.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequestProject {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
