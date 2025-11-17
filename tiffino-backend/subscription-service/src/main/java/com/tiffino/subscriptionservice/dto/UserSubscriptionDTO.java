package com.tiffino.subscriptionservice.dto;

import com.tiffino.subscriptionservice.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class UserSubscriptionDTO {
    private Long id;
    private Long userId;
    private SubscriptionPlanDTO subscriptionPlan;
    private SubscriptionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextRenewalDate;
    private CustomizationDTO customizations;


}