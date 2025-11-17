package com.tiffino.userservice.client;


import com.tiffino.userservice.dto.GiftCardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PAYMENT-SERVICE")
public interface GiftCardClient {

    @GetMapping("/api/giftcards/user/{userId}")
    List<GiftCardResponse> getGiftCardsByUserId(@PathVariable Long userId);
}
