package com.quang.template.controller;

import com.quang.template.dto.request.AuthRequest;
import com.quang.template.dto.request.RegisterRequest;
import com.quang.template.dto.response.ResponseAPI;
import com.quang.template.service.AuthService;
import com.quang.template.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseAPI> register(@RequestBody RegisterRequest request) {
        log.info("Register request received: {}", request.getUsername());
        return ResponseFactory.created(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseAPI> authenticate(@RequestBody AuthRequest request) {
        log.info("Authentication request received: {}", request.getUsername());
        var response = authService.authenticate(request);
        log.info("Authentication successful for: {}", request.getUsername());
        return ResponseFactory.success("Authentication successful", response);
    }
}