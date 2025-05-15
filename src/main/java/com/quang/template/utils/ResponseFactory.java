package com.quang.template.utils;

import com.quang.template.dto.response.ResponseAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactory {

    public static <T> ResponseEntity<ResponseAPI> success(T data) {
        ResponseAPI response = ResponseAPI.builder()
                .message("Success")
                .data(data)
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ResponseAPI> success(String message, T data) {
        ResponseAPI response = ResponseAPI.builder()
                .message(message)
                .data(data)
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<ResponseAPI> created(Object data) {
        ResponseAPI response = ResponseAPI.builder()
                .message("Created successfully")
                .data(data)
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static ResponseEntity<ResponseAPI> noContent() {
        ResponseAPI response = ResponseAPI.builder()
                .message("No content")
                .data(null)
                .status(HttpStatus.NO_CONTENT.value())
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    public static ResponseEntity<ResponseAPI> badRequest(String message) {
        ResponseAPI response = ResponseAPI.builder()
                .message(message)
                .data(null)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public static ResponseEntity<ResponseAPI> notFound(String message) {
        ResponseAPI response = ResponseAPI.builder()
                .message(message)
                .data(null)
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    public static ResponseEntity<ResponseAPI> internalServerError(String message) {
        ResponseAPI response = ResponseAPI.builder()
                .message(message)
                .data(null)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}