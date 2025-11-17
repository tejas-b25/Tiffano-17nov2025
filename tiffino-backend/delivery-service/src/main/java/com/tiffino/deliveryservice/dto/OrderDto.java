package com.tiffino.deliveryservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OrderDto
{
    private Long id;
    private Long userId;
    private String orderType;
    private Long subscriptionId;
    private LocalDateTime orderDate;
    private LocalDate deliveryDate;
    private String deliveryTimeSlot;
    private BigDecimal totalAmount;
    private String status;
    private Long deliveryAddressId;
}
