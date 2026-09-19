package com.api_task_management.project.constant;

import java.util.Set;

public class ProjectSortFields {
    private ProjectSortFields() {}

    public static final Set<String> ALLOWED_FIELDS = Set.of(
            "id",
            "name",
            "status",
            "startDate",
            "endDate",
            "createdAt",
            "updatedAt"
    );
}
