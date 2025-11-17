package com.tiffino.userservice.client;


import com.tiffino.userservice.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {
    @GetMapping("/api/orders/user/{userId}")
    List<OrderResponse> getOrdersByUserId(@PathVariable Long userId);
}
