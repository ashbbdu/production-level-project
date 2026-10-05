package com.api_task_management.comment.service;

import com.api_task_management.comment.dto.request.CreateCommentRequest;
import com.api_task_management.comment.dto.response.CommentResponse;
import com.api_task_management.comment.entity.CommentEntity;
import com.api_task_management.comment.repository.CommentRepository;
import com.api_task_management.common.exception.ResourceNotFoundException;
import com.api_task_management.task.entity.TaskEntity;
import com.api_task_management.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public CommentResponse toResponse (CommentEntity comment) {
        CommentResponse response = new CommentResponse();

        response.setId(comment.getId());
        response.setDescription(comment.getDescription());
        response.setTaskId(comment.getTask().getId());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());

        return response;
    }


    @Override
    public CommentResponse createComment(CreateCommentRequest request , Long taskId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new
                ResourceNotFoundException("Task with id : " + taskId + " not found !"));

        CommentEntity comment = new CommentEntity();
        comment.setDescription(request.getDescription());

        CommentEntity savedComment = commentRepository.save(comment);


        return toResponse(savedComment);

    }
}
