package com.tiffino.loyaltyservice.service.impl;

import com.tiffino.loyaltyservice.dto.LoyaltyPointDto;
import com.tiffino.loyaltyservice.entity.LoyaltyPoint;
import com.tiffino.loyaltyservice.enums.LoyaltyTier;
import com.tiffino.loyaltyservice.exception.ResourceNotFoundException;
import com.tiffino.loyaltyservice.repository.LoyaltyPointRepository;
import com.tiffino.loyaltyservice.service.LoyaltyPointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoyaltyPointServiceImpl implements LoyaltyPointService {

    private final LoyaltyPointRepository loyaltyRepo;

    private LoyaltyTier calculateTier(int totalPoints) {
        if (totalPoints >= 2000) return LoyaltyTier.PLATINUM;
        if (totalPoints >= 1000) return LoyaltyTier.GOLD;
        if (totalPoints >= 500) return LoyaltyTier.SILVER;
        return LoyaltyTier.BRONZE;
    }

    @Override
    public LoyaltyPointDto createLoyaltyPoint(LoyaltyPointDto dto) {
        LoyaltyPoint entity = LoyaltyPoint.builder()
                .userId(dto.getUserId())
                .currentPoints(dto.getCurrentPoints())
                .totalEarnedPoints(dto.getTotalEarnedPoints())
                .lastUpdated(LocalDateTime.now())
                .loyaltyTier(calculateTier(dto.getTotalEarnedPoints()))
                .build();
        LoyaltyPoint saved = loyaltyRepo.save(entity);
        LoyaltyPointDto result = new LoyaltyPointDto();
        BeanUtils.copyProperties(saved, result);
        return result;
    }

    @Override
    public LoyaltyPointDto getLoyaltyPointByUserId(Long userId) {
        LoyaltyPoint entity = loyaltyRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Loyalty Point not found for userId: " + userId));
        LoyaltyPointDto dto = new LoyaltyPointDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<LoyaltyPointDto> getAllLoyaltyPoints() {
        return loyaltyRepo.findAll().stream().map(entity -> {
            LoyaltyPointDto dto = new LoyaltyPointDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public LoyaltyPointDto updateLoyaltyPoint(Long userId, LoyaltyPointDto dto) {
        LoyaltyPoint existing = loyaltyRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Loyalty Point not found for userId: " + userId));

        int oldPoints = existing.getCurrentPoints() != null ? existing.getCurrentPoints() : 0;
        int newPoints = dto.getCurrentPoints() != null ? dto.getCurrentPoints() : oldPoints;

        existing.setCurrentPoints(newPoints);
        int currentTotal = existing.getTotalEarnedPoints() != null ? existing.getTotalEarnedPoints() : 0;
        existing.setTotalEarnedPoints(currentTotal + newPoints);

        existing.setLastUpdated(LocalDateTime.now());
        existing.setLoyaltyTier(calculateTier(existing.getTotalEarnedPoints()));

        LoyaltyPoint updated = loyaltyRepo.save(existing);
        LoyaltyPointDto result = new LoyaltyPointDto();
        BeanUtils.copyProperties(updated, result);
        return result;
    }


    @Transactional
    @Override
    public String deleteLoyaltyPoint(Long userId) {
        long deletedCount = loyaltyRepo.deleteByUserId(userId);
        if (deletedCount == 0) {
            throw new ResourceNotFoundException("No loyalty point found for userId: " + userId);
        }
        return "Loyalty points for userId " + userId + " have been successfully deleted.";
    }

}
