package com.tiffino.userservice.client;

import com.tiffino.userservice.dto.LoyaltyPointResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "LOYALTY-SERVICE")
public interface LoyaltyPointClient {
    @GetMapping("/api/loyalty/user/{userId}")
    LoyaltyPointResponse getByUserId(@PathVariable Long userId);
}
