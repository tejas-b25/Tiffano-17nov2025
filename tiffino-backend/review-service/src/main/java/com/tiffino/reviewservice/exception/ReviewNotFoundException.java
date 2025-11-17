package com.tiffino.reviewservice.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String Message) {
        super(Message);
    }
}
