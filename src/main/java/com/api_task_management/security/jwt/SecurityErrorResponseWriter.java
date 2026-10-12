package com.api_task_management.security.jwt;

import com.api_task_management.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SecurityErrorResponseWriter {
    private final ObjectMapper objectMapper;

    public void writeError (
            HttpServletRequest request,
            HttpServletResponse response,
            int status,
            String error,
            String message
    )  throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status,
                error,
                message,
                request.getRequestURI(),
                null
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);

    }

}
