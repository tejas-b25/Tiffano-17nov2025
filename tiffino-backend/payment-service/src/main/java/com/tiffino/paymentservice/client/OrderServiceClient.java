package com.tiffino.paymentservice.client;

import com.tiffino.paymentservice.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ORDER-SERVICE", path = "/orders")
public interface OrderServiceClient {

    @GetMapping("/{orderId}")
    OrderDTO getOrderById(@PathVariable("orderId") Long orderId);
}
