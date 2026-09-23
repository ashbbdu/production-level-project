package com.api_task_management.task.specification;

import com.api_task_management.task.dto.request.TaskFilterRequest;
import com.api_task_management.task.dto.type.TaskStatus;
import com.api_task_management.task.entity.TaskEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {
    public static Specification<TaskEntity> filter (Long projectId ,TaskFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();


//            TaskEntity
//   │
//   └── project          ← Java field in TaskEntity
//         │
//         └── id         ← Java field in ProjectEntity

            if(projectId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("project").get("id"),  //explanation above
                        projectId
                ));
            }

            if(filter.getStatus() != null) {
             predicates.add(criteriaBuilder
                        .equal(root.get("status"),
                                filter.getStatus()
                        )
                        );
            }

            if(filter.getPriority() != null) {
                predicates.add(criteriaBuilder
                        .equal(root.get("priority"),
                                filter.getPriority()
                        ));
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };

    }
}
