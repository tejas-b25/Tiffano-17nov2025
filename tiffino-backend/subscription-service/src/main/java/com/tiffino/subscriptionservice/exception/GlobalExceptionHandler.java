package com.tiffino.subscriptionservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSubscriptionNotFound(
            SubscriptionNotFoundException ex,
            HttpServletRequest request) {

        return new ResponseEntity<>(
                new ErrorResponse(LocalDateTime.now(), 404, ex.getMessage(), request.getRequestURI()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(SubscriptionPlanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlanNotFound(
            SubscriptionPlanNotFoundException ex,
            HttpServletRequest request) {

        return new ResponseEntity<>(
                new ErrorResponse(LocalDateTime.now(), 404, ex.getMessage(), request.getRequestURI()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        // Log full exception for visibility
        ex.printStackTrace(); // Or use log.error("Unhandled exception", ex);

        return new ResponseEntity<>(
                new ErrorResponse(
                        LocalDateTime.now(),
                        500,
                        ex.getMessage(), // ← Show actual error (use cautiously)
                        request.getRequestURI()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input");

        return new ResponseEntity<>(
                new ErrorResponse(LocalDateTime.now(), 400, errorMessage, request.getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
    }
}