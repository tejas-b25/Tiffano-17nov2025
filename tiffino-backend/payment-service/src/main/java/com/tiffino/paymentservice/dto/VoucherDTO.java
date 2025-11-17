package com.tiffino.paymentservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDTO {

    private Long id;

    private String code;

    private String discountType; // PERCENTAGE or FLAT_AMOUNT
    private BigDecimal discountValue;

    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;

    private LocalDate expiryDate;

    private Integer usageLimit;
    private Integer currentUses;

    private Boolean isActive;

    private Long applicableCuisineId;
}
