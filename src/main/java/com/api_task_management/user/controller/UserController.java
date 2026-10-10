package com.api_task_management.user.controller;

import com.api_task_management.common.advice.ApiResponse;
import com.api_task_management.user.dto.request.CreateUserRequest;
import com.api_task_management.user.dto.response.UserResponse;
import com.api_task_management.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URL;

@RestController
@RequestMapping("/api/auth/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserResponse>> createUser (@RequestBody @Valid CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        ApiResponse<UserResponse> data = new ApiResponse<>(
                true,
                "User created successfully !",
                response
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(data);
    }
}
