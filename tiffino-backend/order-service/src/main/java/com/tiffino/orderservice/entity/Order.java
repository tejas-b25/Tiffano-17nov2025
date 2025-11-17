package com.tiffino.orderservice.entity;

import com.tiffino.orderservice.enums.*;
import jakarta.persistence.*;
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
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    private Boolean isSubscription;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    private Long userSubscriptionId;

    private LocalDateTime orderDate;
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    private DeliveryTimeSlot deliveryTimeSlot;

    private BigDecimal totalAmount;

    // ✅ Addresses (moved into a separate entity)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    // ✅ Order items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
