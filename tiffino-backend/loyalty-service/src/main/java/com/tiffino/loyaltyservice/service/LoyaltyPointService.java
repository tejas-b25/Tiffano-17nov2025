package com.tiffino.loyaltyservice.service;

import com.tiffino.loyaltyservice.dto.LoyaltyPointDto;

import java.util.List;

public interface LoyaltyPointService {
    LoyaltyPointDto createLoyaltyPoint(LoyaltyPointDto dto);
    LoyaltyPointDto getLoyaltyPointByUserId(Long userId);
    List<LoyaltyPointDto> getAllLoyaltyPoints();
    LoyaltyPointDto updateLoyaltyPoint(Long userId, LoyaltyPointDto dto);
    String deleteLoyaltyPoint(Long userId);
//    LoyaltyPointDto addPoints(Long userId, int points);
}