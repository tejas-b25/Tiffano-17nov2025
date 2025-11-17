package com.tiffino.deliveryservice.client;

import com.tiffino.deliveryservice.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ORDER-SERVICE",// temporary placeholder
        fallback = OrderClientFallback.class
)
public interface OrderClient
{
    @GetMapping("/api/orders/{orderId}")
    OrderDto getOrderById(@PathVariable("orderId") Long orderId);
}
