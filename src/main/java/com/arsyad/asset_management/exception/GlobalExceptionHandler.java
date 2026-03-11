package com.arsyad.asset_management.exception;

import com.arsyad.asset_management.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("Bad Request")
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(403)
                        .error("Forbidden")
                        .message("You don't have permission")
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request
    ) {

        ex.printStackTrace(); // supaya muncul di console

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(500)
                        .error("Internal Server Error")
                        .message(ex.getMessage()) // tampilkan pesan asli
                        .path(request.getRequestURI())
                        .build());
    }

}
