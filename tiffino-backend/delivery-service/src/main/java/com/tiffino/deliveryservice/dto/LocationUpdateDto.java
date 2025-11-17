package com.tiffino.deliveryservice.dto;

import lombok.Data;

@Data
public class LocationUpdateDto
{
    private Long orderId;
    private Double latitude;
    private Double longitude;
}
