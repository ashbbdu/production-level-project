package com.api_task_management.task.dto.response;

import com.api_task_management.task.dto.type.TaskPriority;
import com.api_task_management.task.dto.type.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskListResponse {
    private Long id;
    private String title;
    private TaskStatus status;
    private TaskPriority priority;
    private Long projectId;
    private String projectName;
}
