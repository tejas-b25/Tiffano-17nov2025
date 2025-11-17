package com.tiffino.subscriptionservice.entity;

import com.tiffino.subscriptionservice.enums.BillingCycle;
import com.tiffino.subscriptionservice.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private LocalDate nextRenewalDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private Long deliveryAddressId;

    @Column(columnDefinition = "TEXT")
    private String customizations;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle")
    private BillingCycle billingCycle;

    private Boolean autoRenew;

    private Integer totalInstallments;
    private Integer currentInstallment;
    private BigDecimal installmentAmount;
    private LocalDate nextInstallmentDate;
    private Boolean isInstallmentActive;
}