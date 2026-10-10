package com.api_task_management.auth.service;

import com.api_task_management.auth.dto.request.LoginRequest;
import com.api_task_management.auth.dto.response.LoginResponse;

public interface AuthService {
    public LoginResponse login (LoginRequest request);
}
