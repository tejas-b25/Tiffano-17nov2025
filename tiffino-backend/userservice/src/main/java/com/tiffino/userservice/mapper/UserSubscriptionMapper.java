package com.tiffino.userservice.mapper;

import com.tiffino.userservice.dto.UserSubscriptionResponse;

import java.time.LocalDate;

public class UserSubscriptionMapper {
    public static UserSubscriptionResponse toResponse(Object[] data) {
        return UserSubscriptionResponse.builder()
                .id(((Number) data[0]).longValue())
                .planName((String) data[1])
                .status((String) data[2])
                .startDate((LocalDate) data[3])
                .endDate((LocalDate) data[4])
                .build();
    }
}
