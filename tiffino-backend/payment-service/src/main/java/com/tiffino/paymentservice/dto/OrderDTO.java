package com.tiffino.paymentservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
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
    private Long paymentId;
    private String notes;
    private BigDecimal appliedDiscount;
}
