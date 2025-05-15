package com.quang.template.exception;

import com.quang.template.dto.response.ResponseAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseAPI> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message(ex.getMessage())
                .data(null)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ResponseAPI> handleLockedException(LockedException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Account is locked: " + ex.getMessage())
                .data(null)
                .status(HttpStatus.FORBIDDEN.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseAPI> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Invalid username or password")
                .data(null)
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseAPI> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message(ex.getMessage())
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ResponseAPI> handleDisabledException(DisabledException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message("Account is disabled: " + ex.getMessage())
                .data(null)
                .status(HttpStatus.FORBIDDEN.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseAPI> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message(ex.getMessage())
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseAPI> handleGlobalException(Exception ex, WebRequest request) {
        ResponseAPI apiResponse = ResponseAPI.builder()
                .message(ex.getMessage())
                .data(null)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}