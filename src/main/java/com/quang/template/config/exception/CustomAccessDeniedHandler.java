package com.quang.template.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quang.template.dto.response.ResponseAPI;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Xử lý trường hợp đã xác thực nhưng không có quyền truy cập (FORBIDDEN - 403)
 * Kích hoạt khi người dùng đã đăng nhập nhưng không có quyền truy cập tài nguyên
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Access denied: " + accessDeniedException.getMessage())
                .data(null)
                .status(HttpStatus.FORBIDDEN.value())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}