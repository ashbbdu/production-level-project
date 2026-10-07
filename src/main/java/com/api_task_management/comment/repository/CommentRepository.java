package com.api_task_management.comment.repository;

import com.api_task_management.comment.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity , Long> {
     Page<CommentEntity> findAllByTaskId(Long taskId , Pageable pageable);
}
