package com.tiffino.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "delivery_partner_id")
    private DeliveryPartner deliveryPartner;

    private LocalDateTime pickupTime;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Double currentLatitude;
    private Double currentLongitude;

    public enum Status {
        ASSIGNED,
        PICKED_UP,
        EN_ROUTE,
        DELIVERED,
        FAILED
    }

    //and for delievery partner status is either On_delivery or Available
}
