package com.api_task_management.task.dto.response;

import com.api_task_management.task.dto.type.TaskPriority;
import com.api_task_management.task.dto.type.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private Long projectId;
}
