package com.api_task_management.comment.service;

import com.api_task_management.comment.dto.request.CreateCommentRequest;
import com.api_task_management.comment.dto.response.CommentResponse;

public interface CommentService {
    public CommentResponse createComment (CreateCommentRequest request , Long taskId);
}
