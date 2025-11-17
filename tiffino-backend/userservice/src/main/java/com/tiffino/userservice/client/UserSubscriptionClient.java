package com.tiffino.userservice.client;


import com.tiffino.userservice.dto.UserSubscriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "SUBSCRIPTION-SERVICE")
public interface UserSubscriptionClient {

    @GetMapping("/api/subscriptions/user/{userId}")
    List<UserSubscriptionResponse> getSubscriptionsByUserId(@PathVariable Long userId);
}
