package com.tiffino.userservice.client;

import com.tiffino.userservice.dto.ReviewResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "REVIEW-SERVICE")
public interface ReviewClient {
    @GetMapping("/api/reviews/user/{userId}")
    List<ReviewResponse> getReviewsByUserId(@PathVariable Long userId);
}
