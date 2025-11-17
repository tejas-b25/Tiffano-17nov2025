package com.tiffino.loyaltyservice.entity;

import com.tiffino.loyaltyservice.enums.LoyaltyTier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "loyalty_point")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    private Integer currentPoints;
    private Integer totalEarnedPoints;
    private LocalDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    private LoyaltyTier loyaltyTier;
}
