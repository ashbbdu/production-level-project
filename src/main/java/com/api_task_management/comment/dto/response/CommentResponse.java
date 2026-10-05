package com.api_task_management.comment.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String description;
    private Long taskId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
