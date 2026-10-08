package com.api_task_management.user.controller;

import com.api_task_management.user.dto.request.CreateUserRequest;
import com.api_task_management.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @PostMapping("/create")
    public UserResponse createUser (@RequestBody @Valid CreateUserRequest request) {

    }
}
