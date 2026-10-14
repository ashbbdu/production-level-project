package com.api_task_management.user.controller;

import com.api_task_management.common.advice.ApiResponse;
import com.api_task_management.common.response.PageResponse;
import com.api_task_management.user.dto.request.ApproveUserRequest;
import com.api_task_management.user.dto.request.CreateUserRequest;
import com.api_task_management.user.dto.request.UserFilterRequest;
import com.api_task_management.user.dto.response.UserResponse;
import com.api_task_management.user.dto.type.UserRole;
import com.api_task_management.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URL;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/auth/user/create")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{userId}/approve")
    public ResponseEntity<ApiResponse<UserResponse>> approveUser (@PathVariable Long userId , @RequestBody ApproveUserRequest request) {
        UserResponse response = userService.approveUser(userId , request.getRole());
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User approved successfully !",
                        response
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{userId}/disable")
    public ResponseEntity<ApiResponse<UserResponse>> disableUser (@PathVariable Long userId) {
        UserResponse response = userService.disableUser(userId);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User approved successfully !",
                        response
                )
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users/list")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers (
            @PageableDefault (page = 0 , size = 10 ,
            sort = "createdAt" ,direction = Sort.Direction.ASC)Pageable pageable , @ModelAttribute UserFilterRequest filter) {
        PageResponse<UserResponse> response = userService.getAllUsers(pageable, filter);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User list fetched successfully !",
                        response
                )
        );
    }
}
