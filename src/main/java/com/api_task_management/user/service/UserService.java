package com.api_task_management.user.service;

import com.api_task_management.common.response.PageResponse;
import com.api_task_management.permission.entity.PermissionEntity;
import com.api_task_management.user.dto.request.CreateUserRequest;
import com.api_task_management.user.dto.request.UserFilterRequest;
import com.api_task_management.user.dto.response.UserResponse;
import com.api_task_management.user.dto.type.UserRole;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    public UserResponse createUser(CreateUserRequest request);
    public UserResponse approveUser (Long userId , UserRole role);
    public UserResponse disableUser(Long userId);
    public PageResponse<UserResponse> getAllUsers (Pageable pageable , UserFilterRequest filter);

}
