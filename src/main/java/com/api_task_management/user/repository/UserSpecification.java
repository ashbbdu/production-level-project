package com.api_task_management.user.repository;

import com.api_task_management.project.entity.ProjectEntity;
import com.api_task_management.user.dto.request.UserFilterRequest;

import com.api_task_management.user.entity.UserEntity;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification  {
    public static Specification<UserEntity> filter(
            UserFilterRequest filter) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.getEmail() != null) {
                predicates.add(criteriaBuilder.equal(root.get("email") , filter.getEmail()));
            }

            if(filter.getFirstName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("firstName") , filter.getFirstName()));
            }


            if (filter.getStartCreatedDate() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getStartCreatedDate()
                        )
                );
            }

            if (filter.getEndCreatedDate() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getEndCreatedDate()
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
