package com.api_task_management.user.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserFilterRequest {
    private String email;
    private String firstName;
    private LocalDateTime startCreatedDate;
    private LocalDateTime endCreatedDate;
}
