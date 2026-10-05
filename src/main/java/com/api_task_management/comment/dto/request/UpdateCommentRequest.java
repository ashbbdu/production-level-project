package com.api_task_management.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCommentRequest {
    @NotBlank(message = "Comment Description is required !")
    @Size(min = 2 , max = 2000 , message = "Comment must be in 2 to 2000 characters")
    private String description;
}
