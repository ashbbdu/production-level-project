package com.api_task_management.comment.service;

import com.api_task_management.comment.dto.request.CreateCommentRequest;
import com.api_task_management.comment.dto.request.UpdateCommentRequest;
import com.api_task_management.comment.dto.response.CommentResponse;
import com.api_task_management.common.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    public CommentResponse createComment (CreateCommentRequest request , Long taskId);
    public CommentResponse getCommentById(Long commentId);
    public PageResponse<CommentResponse> getCommentsByTask (Long taskId , Pageable pageable);
    public CommentResponse updateComment (UpdateCommentRequest request , Long commentId);
    public void deleteComment (Long commentId);
}
