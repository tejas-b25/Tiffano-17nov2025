package com.tiffino.userservice.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFound(EntityNotFoundException ex) {
        log.error("Entity not found: {}", ex.getMessage());
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException ex) {
        log.error("Bad request: {}", ex.getMessage());
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        String defaultMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        log.error("Validation error: {}", defaultMessage);
        return new ResponseEntity<>(new ApiResponse<>(false, defaultMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneral(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        return new ResponseEntity<>(new ApiResponse<>(false, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Optional: Enum Conversion Handling
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponse<?>> handleEnumConversion(IllegalArgumentException ex) {
        log.error("Enum conversion error: {}", ex.getMessage());
        return new ResponseEntity<>(new ApiResponse<>(false, "Invalid value provided."), HttpStatus.BAD_REQUEST);
    }
}
