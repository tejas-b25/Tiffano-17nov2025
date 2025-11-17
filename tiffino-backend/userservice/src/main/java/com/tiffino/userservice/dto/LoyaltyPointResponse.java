package com.tiffino.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyPointResponse {
    private Long id;
    private Integer totalPoints;
    private Integer redeemedPoints;
    private Integer remainingPoints;
}
