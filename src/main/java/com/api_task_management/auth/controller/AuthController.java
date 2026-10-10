package com.api_task_management.auth.controller;

import com.api_task_management.auth.dto.request.LoginRequest;
import com.api_task_management.auth.dto.response.LoginResponse;
import com.api_task_management.auth.service.AuthService;
import com.api_task_management.common.advice.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/user/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login (@RequestBody @Valid LoginRequest request) {
        System.out.println("LOGIN CONTROLLER REACHED");
        LoginResponse data = authService.login(request);
        ApiResponse<LoginResponse> response = new ApiResponse<>(
                true,
                "User Logged in Successfully !",
                data
        );
        return ResponseEntity.ok(response);
    }
}
