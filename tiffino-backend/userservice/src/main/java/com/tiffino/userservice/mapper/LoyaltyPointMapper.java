package com.tiffino.userservice.mapper;

import com.tiffino.userservice.dto.LoyaltyPointResponse;

public class LoyaltyPointMapper {

    public static LoyaltyPointResponse toResponse(Object[] data) {
        return LoyaltyPointResponse.builder()
                .id(((Number) data[0]).longValue())
                .totalPoints((Integer) data[2])
                .redeemedPoints(((Integer) data[2] - (Integer) data[1]))
                .remainingPoints((Integer) data[1])
                .build();
    }
}
