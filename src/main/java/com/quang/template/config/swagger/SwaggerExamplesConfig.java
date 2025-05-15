package com.quang.template.config.swagger;

import com.quang.template.dto.request.AuthRequest;
import com.quang.template.dto.request.RefreshTokenRequest;
import com.quang.template.dto.request.RegisterRequest;
import com.quang.template.dto.request.user.CreateUserRequest;
import com.quang.template.dto.request.user.UpdateUserRequest;
import com.quang.template.dto.response.AuthResponse;
import com.quang.template.dto.response.user.UserResponse;
import com.quang.template.model.Enum.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Provides example objects for Swagger documentation
 */
@Configuration
public class SwaggerExamplesConfig {

    @Bean
    public Map<String, Object> authenticationExamples() {
        // Auth Request Example
        AuthRequest authRequest = AuthRequest.builder()
                .username("admin")
                .password("admin@123")
                .build();

        // Auth Response Example
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxNTgyODkyOCwiZXhwIjoxNzE1OTE1MzI4fQ.example")
                .refreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxNTgyODkyOCwiZXhwIjoxNzE2NDMzNzI4fQ.refresh-example")
                .build();

        // Register Request Example
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("newuser")
                .email("newuser@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .build();

        // Refresh Token Request Example
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxNTgyODkyOCwiZXhwIjoxNzE2NDMzNzI4fQ.refresh-example")
                .build();

        return Map.of(
                "authRequest", authRequest,
                "authResponse", authResponse,
                "registerRequest", registerRequest,
                "refreshTokenRequest", refreshTokenRequest
        );
    }

    @Bean
    public Map<String, Object> userExamples() {
        // User Create Request Example
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("newuser")
                .email("user@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .build();

        // User Update Request Example
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .username("updateduser")
                .email("updated@example.com")
                .password("newpassword")
                .firstName("Jane")
                .lastName("Smith")
                .role(Role.ADMIN)
                .enabled(true)
                .accountNonLocked(true)
                .build();

        // User Response Example
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .username("admin")
                .email("admin@example.com")
                .firstName("Admin")
                .lastName("User")
                .role(Role.ADMIN)
                .enabled(true)
                .accountNonLocked(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("SYSTEM")
                .updatedBy("admin")
                .build();

        return Map.of(
                "createUserRequest", createUserRequest,
                "updateUserRequest", updateUserRequest,
                "userResponse", userResponse
        );
    }
}