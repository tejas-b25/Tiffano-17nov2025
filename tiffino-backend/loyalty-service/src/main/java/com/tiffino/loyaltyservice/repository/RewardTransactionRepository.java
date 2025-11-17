package com.tiffino.loyaltyservice.repository;

import com.tiffino.loyaltyservice.entity.RewardTransaction;
import com.tiffino.loyaltyservice.enums.RewardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RewardTransactionRepository extends JpaRepository<RewardTransaction, Long> {
    List<RewardTransaction> findByUserId(Long userId);
    List<RewardTransaction> findByStatusAndExpiryDateBefore(RewardStatus status, LocalDateTime expiryDate);
    List<RewardTransaction> findByRelatedOrderId(Long orderId);
}
