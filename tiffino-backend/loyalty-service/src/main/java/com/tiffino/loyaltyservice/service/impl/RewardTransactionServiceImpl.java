package com.tiffino.loyaltyservice.service.impl;

import com.tiffino.loyaltyservice.dto.LoyaltyPointDto;
import com.tiffino.loyaltyservice.dto.RewardTransactionDto;
import com.tiffino.loyaltyservice.entity.RewardTransaction;
import com.tiffino.loyaltyservice.enums.RewardStatus;
import com.tiffino.loyaltyservice.exception.ResourceNotFoundException;
import com.tiffino.loyaltyservice.repository.RewardTransactionRepository;
import com.tiffino.loyaltyservice.service.LoyaltyPointService;
import com.tiffino.loyaltyservice.service.RewardTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardTransactionServiceImpl implements RewardTransactionService {

    private final RewardTransactionRepository rewardTransactionRepository;
    private final LoyaltyPointService loyaltyPointService;

    @Override
    public RewardTransactionDto createRewardTransaction(RewardTransactionDto dto) {
        RewardTransaction entity = new RewardTransaction();
        BeanUtils.copyProperties(dto, entity);

        LocalDateTime now = LocalDateTime.now();
        entity.setTransactionTime(now);

        if ("EARNED".equalsIgnoreCase(dto.getTransactionType()) ||
                "ORDER_EARNED".equalsIgnoreCase(dto.getTransactionType())) {
            entity.setExpiryDate(now.plusDays(90));
        }

        if (dto.getStatus() == null) {
            entity.setStatus(RewardStatus.APPROVED);
        }

        RewardTransaction saved = rewardTransactionRepository.save(entity);

        // Loyalty Point Create or Update
        if (saved.getPointsChange() > 0 && saved.getStatus() == RewardStatus.APPROVED) {
            LoyaltyPointDto loyaltyDto = new LoyaltyPointDto();
            loyaltyDto.setUserId(saved.getUserId());
            loyaltyDto.setCurrentPoints(saved.getPointsChange());
            loyaltyDto.setTotalEarnedPoints(saved.getPointsChange());

            try {
                loyaltyPointService.updateLoyaltyPoint(saved.getUserId(), loyaltyDto);
            } catch (ResourceNotFoundException ex) {
                loyaltyPointService.createLoyaltyPoint(loyaltyDto);
            }
        }

        RewardTransactionDto result = new RewardTransactionDto();
        BeanUtils.copyProperties(saved, result);
        return result;
    }

    @Override
    public RewardTransactionDto getRewardTransactionById(Long id) {
        RewardTransaction entity = rewardTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reward transaction not found"));
        RewardTransactionDto dto = new RewardTransactionDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<RewardTransactionDto> getRewardTransactionsByUserId(Long userId) {
        List<RewardTransaction> list = rewardTransactionRepository.findByUserId(userId);
        return list.stream().map(entity -> {
            RewardTransactionDto dto = new RewardTransactionDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RewardTransactionDto> getAllTransactions() {
        return rewardTransactionRepository.findAll().stream().map(entity -> {
            RewardTransactionDto dto = new RewardTransactionDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public RewardTransactionDto awardReferralBonus(Long userId) {
        RewardTransactionDto dto = new RewardTransactionDto();
        dto.setUserId(userId);
        dto.setPointsChange(100); // Referral Bonus
        dto.setTransactionType("REFERRAL_BONUS");
        dto.setSource("REFERRAL_CAMPAIGN_2025");
        dto.setTransactionTime(LocalDateTime.now());
        dto.setExpiryDate(LocalDateTime.now().plusDays(60));
        dto.setStatus(RewardStatus.APPROVED);
        return createRewardTransaction(dto);
    }

    @Override
    @Transactional
    public void reverseOrderReward(Long orderId) {
        RewardTransaction tx = rewardTransactionRepository.findByRelatedOrderId(orderId).stream()
                .filter(t -> "ORDER_EARNED".equals(t.getTransactionType()) && t.getStatus() == RewardStatus.APPROVED)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No approved ORDER_EARNED reward for orderId: " + orderId));

        // Reward transaction as REFUND
        tx.setStatus(RewardStatus.REFUND);
        tx.setTransactionType("ORDER_REFUND");
        tx.setSource("REFUND_ORDER#" + orderId);
        tx.setTransactionTime(LocalDateTime.now());
        rewardTransactionRepository.save(tx);

        // Update existing loyalty point
        int pointsToDeduct = tx.getPointsChange();

        if (pointsToDeduct > 0) {
            LoyaltyPointDto loyaltyDto = new LoyaltyPointDto();
            loyaltyDto.setUserId(tx.getUserId());
            loyaltyDto.setCurrentPoints(-pointsToDeduct);
            loyaltyDto.setTotalEarnedPoints(0);

            loyaltyPointService.updateLoyaltyPoint(tx.getUserId(), loyaltyDto);

        }
    }

}
