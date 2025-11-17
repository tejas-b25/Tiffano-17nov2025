package com.tiffino.loyaltyservice.dto;

import com.tiffino.loyaltyservice.enums.RewardStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardTransactionDto {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Min(value = -10000)
    private Integer pointsChange;

    @NotNull
    @Size(min = 3, max = 50)
    private String transactionType;

    private String source;

    @NotNull
    private LocalDateTime transactionTime;

    private LocalDateTime expiryDate;

    private RewardStatus status;

    private Long relatedOrderId;
}
