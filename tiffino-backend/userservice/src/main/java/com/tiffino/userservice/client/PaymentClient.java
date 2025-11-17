package com.tiffino.userservice.client;


import com.tiffino.userservice.dto.PaymentTransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PAYMENT-SERVICE")
public interface PaymentClient {

    @GetMapping("/api/payments/user/{userId}")
    List<PaymentTransactionResponse> getTransactionsByUserId(@PathVariable Long userId);
}
