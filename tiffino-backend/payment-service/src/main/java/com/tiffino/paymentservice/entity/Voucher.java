package com.tiffino.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "voucher_new")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String discountType; // PERCENTAGE, FLAT_AMOUNT
    private BigDecimal discountValue;

    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;

    private LocalDate expiryDate;

    private Integer usageLimit;
    private Integer currentUses;

    private Boolean isActive;

    private Long applicableCuisineId;
}
