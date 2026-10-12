package com.api_task_management.security.jwt;

import com.api_task_management.common.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    private final SecurityErrorResponseWriter errorResponseWriter;
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

//        ErrorResponse errorResponse = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpServletResponse.SC_UNAUTHORIZED,
//                "Unauthorized",
//                "Token Missing",
//                request.getRequestURI(),
//                null
//        );
        errorResponseWriter.writeError(
                request,
                response,
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "Token Missing"
        );

//        objectMapper.writeValue(response.getWriter(), errorResponse);

    }
}
