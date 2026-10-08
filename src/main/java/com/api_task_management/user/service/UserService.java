package com.api_task_management.user.service;

import com.api_task_management.user.dto.request.CreateUserRequest;
import com.api_task_management.user.dto.response.UserResponse;
import org.springframework.stereotype.Service;


public interface UserService {
    public UserResponse createUser(CreateUserRequest request);
}
