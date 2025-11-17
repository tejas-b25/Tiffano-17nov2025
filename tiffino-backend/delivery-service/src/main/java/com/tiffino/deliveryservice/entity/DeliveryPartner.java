package com.tiffino.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryPartner
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;
    private String vehicleDetails;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Double currentLatitude;
    private Double currentLongitude;

    public enum Status {
        AVAILABLE,
        ON_DELIVERY,
        OFFLINE

    }
}
