package com.tiffino.orderservice.dto;

import com.tiffino.orderservice.enums.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private Long userId;
    private OrderType orderType;
    private Boolean isSubscription;
    private SubscriptionType subscriptionType;
    private Long userSubscriptionId;
    private LocalDateTime orderDate;
    private LocalDate deliveryDate;
    private DeliveryTimeSlot deliveryTimeSlot;
    private BigDecimal totalAmount;

    private List<AddressDto> addresses;  // ✅ New field
    private List<OrderItemDto> orderItems;
}
