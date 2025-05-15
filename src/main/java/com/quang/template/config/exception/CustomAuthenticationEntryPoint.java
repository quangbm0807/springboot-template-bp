package com.quang.template.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quang.template.dto.response.ResponseAPI;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        logger.error("Authentication failed: {}", authException.getMessage(), authException);

        String message = "Authentication failed";
        if (authException.getCause() != null) {
            message += ": " + authException.getCause().getMessage();
        } else if (authException.getMessage() != null) {
            message += ": " + authException.getMessage();
        }

        ResponseAPI apiResponse = ResponseAPI.builder()
                .message(message)
                .data(null)
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}