package com.tiffino.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCardResponse {

    private Long id;
    private String code;
    private Double balance;
    private String status;
    private LocalDateTime expiryDate;

}
