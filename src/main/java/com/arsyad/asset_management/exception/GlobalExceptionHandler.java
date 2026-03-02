package com.arsyad.asset_management.exception;

import com.arsyad.asset_management.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        log.error("RuntimeException at [{} {}] : {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage(),
                ex
        );

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

        log.warn("AccessDenied at [{} {}] : {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

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

        log.error("Unexpected error at [{} {}]",
                request.getMethod(),
                request.getRequestURI(),
                ex
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(500)
                        .error("Internal Server Error")
                        .message("Something went wrong")
                        .path(request.getRequestURI())
                        .build());
    }


}
