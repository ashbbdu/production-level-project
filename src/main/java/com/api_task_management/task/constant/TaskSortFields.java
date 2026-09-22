package com.api_task_management.task.constant;

import java.util.Set;

public class TaskSortFields {
    private TaskSortFields() {}

    public static final Set<String> ALLOWED_FIELDS = Set.of(
            "id",
            "title",
            "status",
            "priority",
            "dueDate",
            "createdAt",
            "updatedAt"
    );
}
