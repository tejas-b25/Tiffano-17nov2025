package com.tiffino.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "USERSERVICE")
public interface UserClient {

    @GetMapping("/users/{userId}")
    UserDTO getUserById(@PathVariable("userId") Long userId);
}