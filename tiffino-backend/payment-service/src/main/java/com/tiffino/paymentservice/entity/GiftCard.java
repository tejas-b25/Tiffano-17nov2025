package com.tiffino.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.*;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "gift_card")
public class GiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(precision = 15, scale = 2)
    private BigDecimal initialAmount;

    @Column(precision = 15, scale = 2)
    private BigDecimal currentBalance;

    private LocalDate expiryDate;

    private Long issuedToUserId;
    private String issuedToEmail;

    private String status; // ACTIVE, REDEEMED, EXPIRED
}
