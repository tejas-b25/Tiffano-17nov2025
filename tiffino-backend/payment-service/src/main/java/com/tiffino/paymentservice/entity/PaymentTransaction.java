package com.tiffino.paymentservice.entity;

import com.tiffino.paymentservice.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "payment_transaction")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long orderId;
    private Long userId;

    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String transactionId;
    private String status;

    private LocalDateTime transactionDate;

    @Column(name = "voucher_code", nullable = true)
    private String voucherCode;

    @Column(name = "gift_card_code", nullable = true)
    private String giftCardCode;

    @Column(name = "cuisine_id", nullable = true)
    private Long cuisineId;
}
