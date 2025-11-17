package com.tiffino.loyaltyservice.dto;

import com.tiffino.loyaltyservice.enums.LoyaltyTier;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoyaltyPointDto {
    private Long id;
    private Long userId;
    private Integer currentPoints;
    private Integer totalEarnedPoints;
    private LocalDateTime lastUpdated;
    private LoyaltyTier loyaltyTier;
}