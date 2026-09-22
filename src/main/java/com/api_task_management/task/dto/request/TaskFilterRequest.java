package com.api_task_management.task.dto.request;

import com.api_task_management.task.dto.type.TaskPriority;
import com.api_task_management.task.dto.type.TaskStatus;
import lombok.Data;

@Data
public class TaskFilterRequest {
   private TaskStatus status;
   private TaskPriority priority;
}
