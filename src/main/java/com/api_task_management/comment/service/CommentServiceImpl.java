package com.api_task_management.comment.service;

import com.api_task_management.comment.dto.request.CreateCommentRequest;
import com.api_task_management.comment.dto.request.UpdateCommentRequest;
import com.api_task_management.comment.dto.response.CommentResponse;
import com.api_task_management.comment.entity.CommentEntity;
import com.api_task_management.comment.repository.CommentRepository;
import com.api_task_management.common.exception.ResourceNotFoundException;
import com.api_task_management.common.response.PageResponse;
import com.api_task_management.task.entity.TaskEntity;
import com.api_task_management.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    public CommentResponse createComment(CreateCommentRequest request , Long taskId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new
                ResourceNotFoundException("Task with id : " + taskId + " not found !"));

        CommentEntity comment = new CommentEntity();
        comment.setDescription(request.getDescription());
        comment.setTask(task);

        CommentEntity savedComment = commentRepository.save(comment);


        return toResponse(savedComment);

    }

    @Override
    public CommentResponse getCommentById(Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment with id : " + commentId + " not found !"));


        return toResponse(comment);
    }


    @Override
    public PageResponse<CommentResponse> getCommentsByTask(Long taskId , Pageable pageable) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() ->
                new ResourceNotFoundException("Task with id " + taskId
                        + " not found !"));

//        work on specification , validate sort etc
        Page<CommentEntity> comments = commentRepository.findAllByTaskId(taskId , pageable);


        List<CommentResponse> response =
                comments.stream().map(this::toResponse).toList();

//        PageResponse<CommentResponse> resp = new PageResponse<>(
//                response,
//                comments.getNumber(),
//                comments.getSize(),
//                comments.getTotalElements(),
//                comments.getTotalPages(),
//                comments.hasNext(),
//                comments.hasPrevious()
//        );

        return  new PageResponse<>(
                response,
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.hasNext(),
                comments.hasPrevious()
        );

    }

    @Override
    @Transactional
    public CommentResponse updateComment(UpdateCommentRequest request , Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() ->
                    new ResourceNotFoundException("Comment with id : " + commentId + " not found")
                );

        comment.setDescription(request.getDescription());

        return toResponse(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment with id : " + commentId + " not found")
        );
        commentRepository.delete(comment);
    }
}
