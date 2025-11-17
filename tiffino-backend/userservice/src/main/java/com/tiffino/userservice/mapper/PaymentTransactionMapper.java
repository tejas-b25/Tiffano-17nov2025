package com.tiffino.userservice.mapper;

import com.tiffino.userservice.dto.PaymentTransactionResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentTransactionMapper {
    public static PaymentTransactionResponse toResponse(Object[] data) {
        return PaymentTransactionResponse.builder()
                .id(((Number) data[0]).longValue())
                .transactionId((String) data[1])
                .amount(((BigDecimal) data[2]).doubleValue())
                .status((String) data[3])
                .transactionDate((LocalDateTime) data[4])
                .build();
    }
}
