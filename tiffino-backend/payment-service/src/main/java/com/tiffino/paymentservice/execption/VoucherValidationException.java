package com.tiffino.paymentservice.execption;

public class VoucherValidationException extends RuntimeException {
    public VoucherValidationException(String message) {
        super(message);
    }
}
