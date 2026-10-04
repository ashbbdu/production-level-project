package com.api_task_management.task.repository;

import com.api_task_management.project.entity.ProjectEntity;
import com.api_task_management.task.dto.request.TaskFilterRequest;
import com.api_task_management.task.dto.response.TaskListResponse;
import com.api_task_management.task.entity.TaskEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class TaskQueryRepositoryImpl implements TaskQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<TaskListResponse> findTaskList(
            Long projectId,
            TaskFilterRequest filter,
            Pageable pageable
    ) {

        CriteriaBuilder criteriaBuilder =
                entityManager.getCriteriaBuilder();

        /*
         * =========================
         * DATA QUERY
         * =========================
         */

        CriteriaQuery<TaskListResponse> query =
                criteriaBuilder.createQuery(TaskListResponse.class);

        Root<TaskEntity> root =
                query.from(TaskEntity.class);

        Join<TaskEntity, ProjectEntity> project =
                root.join("project", JoinType.INNER);

        // SELECT
        query.select(
                criteriaBuilder.construct(
                        TaskListResponse.class,
                        root.get("id"),
                        root.get("title"),
                        root.get("status"),
                        root.get("priority"),
                        project.get("id"),
                        project.get("name")
                )
        );

        // WHERE
        List<Predicate> predicates = new ArrayList<>();

        // Required project scope
        predicates.add(
                criteriaBuilder.equal(
                        project.get("id"),
                        projectId
                )
        );

        // Optional status filter
        if (filter != null && filter.getStatus() != null) {
            predicates.add(
                    criteriaBuilder.equal(
                            root.get("status"),
                            filter.getStatus()
                    )
            );
        }

        // Optional priority filter
        if (filter != null && filter.getPriority() != null) {
            predicates.add(
                    criteriaBuilder.equal(
                            root.get("priority"),
                            filter.getPriority()
                    )
            );
        }

        query.where(
                criteriaBuilder.and(
                        predicates.toArray(new Predicate[0])
                )
        );

        // ORDER BY
        List<Order> orders = new ArrayList<>();

        for (Sort.Order sortOrder : pageable.getSort()) {

            Path<Object> path =
                    root.get(sortOrder.getProperty());

            if (sortOrder.isAscending()) {
                orders.add(
                        criteriaBuilder.asc(path)
                );
            } else {
                orders.add(
                        criteriaBuilder.desc(path)
                );
            }
        }

        if (!orders.isEmpty()) {
            query.orderBy(orders);
        }

        // PAGINATION
        int offset =
                pageable.getPageNumber()
                        * pageable.getPageSize();

        TypedQuery<TaskListResponse> typedQuery =
                entityManager.createQuery(query);

        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(pageable.getPageSize());

        List<TaskListResponse> content =
                typedQuery.getResultList();


        /*
         * =========================
         * COUNT QUERY
         * =========================
         */

        CriteriaQuery<Long> countQuery =
                criteriaBuilder.createQuery(Long.class);

        Root<TaskEntity> countRoot =
                countQuery.from(TaskEntity.class);

        Join<TaskEntity, ProjectEntity> countProject =
                countRoot.join("project", JoinType.INNER);

        List<Predicate> countPredicates =
                new ArrayList<>();

        // Required project scope
        countPredicates.add(
                criteriaBuilder.equal(
                        countProject.get("id"),
                        projectId
                )
        );

        // Optional status filter
        if (filter != null && filter.getStatus() != null) {
            countPredicates.add(
                    criteriaBuilder.equal(
                            countRoot.get("status"),
                            filter.getStatus()
                    )
            );
        }

        // Optional priority filter
        if (filter != null && filter.getPriority() != null) {
            countPredicates.add(
                    criteriaBuilder.equal(
                            countRoot.get("priority"),
                            filter.getPriority()
                    )
            );
        }

        countQuery.select(
                criteriaBuilder.count(countRoot)
        );

        countQuery.where(
                criteriaBuilder.and(
                        countPredicates.toArray(
                                new Predicate[0]
                        )
                )
        );

        Long totalElements =
                entityManager
                        .createQuery(countQuery)
                        .getSingleResult();

        return new PageImpl<>(
                content,
                pageable,
                totalElements
        );
    }
}