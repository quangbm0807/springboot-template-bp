package com.quang.template.controller;

import com.quang.template.dto.request.AuthRequest;
import com.quang.template.dto.request.RefreshTokenRequest;
import com.quang.template.dto.request.RegisterRequest;
import com.quang.template.dto.response.ResponseAPI;
import com.quang.template.service.AuthService;
import com.quang.template.utils.ResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management API")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with USER role"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - validation error or user already exists")
    })
    public ResponseEntity<ResponseAPI> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request received: {}", request.getUsername());
        return ResponseFactory.created(authService.register(request));
    }

    @PostMapping("/authenticate")
    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user and returns access and refresh tokens"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Authentication failed - bad credentials"),
            @ApiResponse(responseCode = "403", description = "Account locked or disabled")
    })
    public ResponseEntity<ResponseAPI> authenticate(@Valid @RequestBody AuthRequest request) {
        log.info("Authentication request received: {}", request.getUsername());
        var response = authService.authenticate(request);
        log.info("Authentication successful for: {}", request.getUsername());
        return ResponseFactory.success("Authentication successful", response);
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Refresh access token",
            description = "Use a valid refresh token to get a new access token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New tokens generated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    public ResponseEntity<ResponseAPI> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Token refresh request received");
        var response = authService.refreshToken(request);
        log.info("Token refresh successful");
        return ResponseFactory.success("Token refresh successful", response);
    }
}