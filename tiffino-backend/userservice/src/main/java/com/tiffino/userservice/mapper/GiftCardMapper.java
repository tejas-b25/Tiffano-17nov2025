package com.tiffino.userservice.mapper;

import com.tiffino.userservice.dto.GiftCardResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GiftCardMapper {

    public static GiftCardResponse toResponse(Object[] data) {
        return GiftCardResponse.builder()
                .id(((Number) data[0]).longValue())
                .code((String) data[1])
                .balance(((BigDecimal) data[3]).doubleValue()) // currentBalance
                .status((String) data[5])
                .expiryDate((LocalDateTime) data[4])
                .build();
    }
}
