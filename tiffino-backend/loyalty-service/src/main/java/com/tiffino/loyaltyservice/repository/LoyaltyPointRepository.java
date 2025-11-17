package com.tiffino.loyaltyservice.repository;

import com.tiffino.loyaltyservice.entity.LoyaltyPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, Long> {
    Optional<LoyaltyPoint> findByUserId(Long userId);
    long  deleteByUserId(Long userId);
}