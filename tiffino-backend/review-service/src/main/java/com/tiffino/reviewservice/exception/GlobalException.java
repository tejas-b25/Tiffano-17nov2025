package com.tiffino.reviewservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ReviewNotFoundException exception){
        return new ResponseEntity<>( "Global Handler:" + exception.getMessage(), HttpStatus.NOT_FOUND );
    }

    // Handle Generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex){
        return new ResponseEntity<>("Global Error:" + ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}