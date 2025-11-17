package com.tiffino.loyaltyservice.service;

import com.tiffino.loyaltyservice.dto.RewardTransactionDto;

import java.util.List;

public interface RewardTransactionService {

    RewardTransactionDto createRewardTransaction(RewardTransactionDto dto);
    RewardTransactionDto getRewardTransactionById(Long id);
    List<RewardTransactionDto> getRewardTransactionsByUserId(Long userId);
    List<RewardTransactionDto> getAllTransactions();
    RewardTransactionDto awardReferralBonus(Long userId);
    void reverseOrderReward(Long orderId);
}
