package com.tiffino.deliveryservice.dto;
import lombok.Data;

@Data
public class DeliveryStatusUpdateDto
{
    private Long orderId;
    private String status; // e.g., "EN_ROUTE", "DELIVERED"
}
