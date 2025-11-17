package com.tiffino.subscriptionservice.exception;

public class SubscriptionPlanNotFoundException extends RuntimeException {
    public SubscriptionPlanNotFoundException(String message) {
        super(message);
    }
}