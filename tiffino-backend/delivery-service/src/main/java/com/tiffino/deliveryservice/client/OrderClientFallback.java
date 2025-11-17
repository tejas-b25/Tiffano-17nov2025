package com.tiffino.deliveryservice.client;

import com.tiffino.deliveryservice.dto.OrderDto;

public class OrderClientFallback implements OrderClient
{
    @Override
    public OrderDto getOrderById(Long orderId) {
        System.out.println("⚠️ Order Service is down. Returning fallback data.");
        return null;  // or throw custom exception, or return mock OrderDto if needed
    }
}
