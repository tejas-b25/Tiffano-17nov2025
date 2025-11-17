package com.tiffino.userservice.mapper;

import com.tiffino.userservice.dto.OrderResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderMapper {
    public static OrderResponse toResponse(Object[] data) {
        return OrderResponse.builder()
                .id(((Number) data[0]).longValue())
                .orderNumber((String) data[1])
                .status((String) data[2])
                .totalAmount(((BigDecimal) data[3]).doubleValue())
                .orderDate((LocalDateTime) data[4])
                .build();
    }
}
