package com.api_task_management.task.repository;

import com.api_task_management.task.dto.type.TaskPriority;
import com.api_task_management.task.dto.type.TaskStatus;
import com.api_task_management.task.entity.TaskEntity;
import lombok.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity , Long> , JpaSpecificationExecutor<TaskEntity> ,
        TaskQueryRepository {
//    public Page<TaskEntity> findByProjectId(Long projectId , Pageable pageable);
public Page<TaskEntity> findByProjectId(Specification<TaskEntity> spec, Pageable pageable);

    @Query("""
    SELECT t
    FROM TaskEntity t
    JOIN FETCH t.project
""")
    List<TaskEntity> findAllWithProject();

//    @EntityGraph(attributePaths = "project")
//    Page<TaskEntity> findByProject_Id(
//            Long projectId,
//            Pageable pageable
//    );

//    ask question that why @EntityGraph is using findAll and    @Query is using a customer name
    @Override
    @EntityGraph(attributePaths = "project")
    Page<TaskEntity> findAll(
           Specification<TaskEntity> specification,
           Pageable pageable
    );

    @Query("""
    SELECT t
    FROM TaskEntity t
    JOIN FETCH t.project p
    WHERE p.id = :projectId
      AND (:status IS NULL OR t.status = :status)
      AND (:priority IS NULL OR t.priority = :priority)
""")
    Page<TaskEntity> findTasksWithProject(
            @Param("projectId") Long projectId,
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            Pageable pageable
    );


//    2 query solution

    List<TaskEntity> findByProjectIdIn(List<Long> projectIds);
}
