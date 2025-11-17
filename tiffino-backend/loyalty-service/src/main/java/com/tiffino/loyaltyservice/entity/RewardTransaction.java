package com.tiffino.loyaltyservice.entity;

import com.tiffino.loyaltyservice.enums.RewardStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reward_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Integer pointsChange; // positive = earned, negative = redeemed

    private String transactionType; // EARNED, REDEEMED, REFERRAL_BONUS, MILESTONE, STREAK, SEASONAL

    private String source; // e.g., "REFERRAL", "ORDER#1234", "STREAK_DAY_5"

    @Enumerated(EnumType.STRING)
    private RewardStatus status;

    @Column(name = "transaction_date")
    private LocalDateTime transactionTime;

    private LocalDateTime expiryDate;
    @Column(name = "order_id")
    private Long relatedOrderId; // Optional, for ORDER_EARNED or REFUND_REVERSED
}
