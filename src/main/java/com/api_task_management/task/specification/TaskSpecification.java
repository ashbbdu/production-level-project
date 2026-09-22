package com.api_task_management.task.specification;

import com.api_task_management.task.dto.request.TaskFilterRequest;
import com.api_task_management.task.dto.type.TaskStatus;
import com.api_task_management.task.entity.TaskEntity;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {
    public static Specification<TaskEntity> filter (TaskFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {

            if(filter.getStatus() != null) {
               return criteriaBuilder
                        .equal(root.get("status"),
                                filter.getStatus()
                        );
            }

            if(filter.getPriority() != null) {
                return criteriaBuilder
                        .equal(root.get("priority"),
                                filter.getPriority()
                        );
            }

            return null;
        };

    }
}
