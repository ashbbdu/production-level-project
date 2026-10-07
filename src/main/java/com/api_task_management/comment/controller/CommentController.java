package com.api_task_management.comment.controller;

import com.api_task_management.comment.dto.request.CreateCommentRequest;
import com.api_task_management.comment.dto.response.CommentResponse;
import com.api_task_management.comment.service.CommentService;
import com.api_task_management.common.advice.ApiResponse;
import com.api_task_management.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment
            (@RequestBody @Valid CreateCommentRequest request , @PathVariable Long taskId) {
        CommentResponse comment = commentService.createComment(request ,taskId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(comment.getId())
                .toUri();

        ApiResponse<CommentResponse> response = new ApiResponse<>(
                true,
                "Comment added successfully !",
                comment
        );

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/tasks/comment/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment
            (@PathVariable Long commentId) {
        CommentResponse comment = commentService.getCommentById(commentId);

        ApiResponse<CommentResponse> response = new ApiResponse<>(
                true,
                "Comment added successfully !",
                comment
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<PageResponse<CommentResponse>>> getCommentsByTask
            (@PathVariable Long taskId , @PageableDefault(size = 10 , page = 0) Pageable pageable) {
        PageResponse<CommentResponse> comments = commentService.getCommentsByTask(taskId , pageable);
        System.out.println(pageable.getSort());
        System.out.println(pageable.getPageNumber());
        ApiResponse<PageResponse<CommentResponse>> response = new ApiResponse<>(
                true,
                "Comments Fetched Successfully !",
                comments
        );

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/comment/delete/{commentId}")
    public ResponseEntity<Void> deleteComment (@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
//        ApiResponse<?> response = new  ApiResponse<>(
//                true,
//                "Comment deleted successfully !",
//                null
//        );

        return ResponseEntity.noContent().build();
    }


}
