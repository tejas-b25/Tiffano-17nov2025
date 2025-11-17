package com.tiffino.deliveryservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignDeliveryRequest
{
    private Long orderId;
   // private LocalDateTime estimatedDeliveryTime;
}
