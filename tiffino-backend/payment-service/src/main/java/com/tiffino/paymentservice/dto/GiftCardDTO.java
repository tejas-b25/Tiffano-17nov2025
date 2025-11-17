package com.tiffino.paymentservice.dto;


import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class GiftCardDTO {

    private Long id;

    private String code;

    private BigDecimal initialAmount;
    private BigDecimal currentBalance;

    private LocalDate expiryDate;

    private Long issuedToUserId;
    private String issuedToEmail;

    private String status; // ACTIVE, REDEEMED, EXPIRED
}
