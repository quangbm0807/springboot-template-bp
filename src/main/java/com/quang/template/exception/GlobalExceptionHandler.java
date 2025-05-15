package com.quang.template.exception;

import com.quang.template.dto.response.ResponseAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseAPI> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Data is invalid")
                .data(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseAPI> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Data is invalid: " + ex.getMessage())
                .data(null)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ResponseAPI> handleLockedException(LockedException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Account is locked")
                .data(null)
                .status(HttpStatus.FORBIDDEN.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseAPI> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Incorrect username or password")
                .data(null)
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseAPI> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Not found username")
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ResponseAPI> handleDisabledException(DisabledException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Account is disabled")
                .data(null)
                .status(HttpStatus.FORBIDDEN.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseAPI> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String resourceName = extractResourceName(ex.getMessage());

        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Not found " + resourceName)
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseAPI> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message(ex.getMessage())
                .data(null)
                .status(HttpStatus.FORBIDDEN.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseAPI> handleGlobalException(Exception ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Error: " + ex.getMessage())
                .data(null)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractResourceName(String message) {
        String resourceName = "Resource";
        if (message != null && message.contains("not found with")) {
            String[] parts = message.split("not found with");
            if (parts.length > 0 && !parts[0].trim().isEmpty()) {
                resourceName = parts[0].trim().toLowerCase();
            }
        }

        return resourceName;
    }
}