package com.example.demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMovieNotFound(MovieNotFoundException e, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                "NOT_FOUND",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                "BAD_REQUEST",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ExternalApiException.class)  // 👈 부모로 통합!
    public ResponseEntity<ErrorResponse> handleExternalApi(ExternalApiException e, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                "EXTERNAL_API_ERROR",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleDefault(Exception e, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                "ERROR",
                e.getMessage(),
                request.getRequestURI()   // ← path 추가됨
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
