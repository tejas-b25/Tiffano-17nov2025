package com.tiffino.userservice.mapper;

import com.tiffino.userservice.dto.ReviewResponse;

public class ReviewMapper {
    public static ReviewResponse toResponse(Object[] data) {
        return ReviewResponse.builder()
                .id(((Number) data[0]).longValue())
                .rating((Integer) data[1])
                .comment((String) data[2])
                .reviewedItem((String) data[3]) // can be meal name or ID
                .build();
    }
}
