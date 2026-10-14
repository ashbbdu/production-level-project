package com.api_task_management.user.dto.request;

import com.api_task_management.user.dto.type.UserRole;
import lombok.Data;

@Data
public class ApproveUserRequest {
    private UserRole role;
}
