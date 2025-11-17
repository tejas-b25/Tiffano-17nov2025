package com.tiffino.paymentservice.service;

import com.tiffino.paymentservice.dto.GiftCardDTO;
import java.math.BigDecimal;
import java.util.List;

public interface GiftCardService {
    GiftCardDTO issueGiftCard(GiftCardDTO dto);
    GiftCardDTO redeemGiftCard(String code, BigDecimal amount);
    List<GiftCardDTO> getGiftCardsByUser(Long userId);
}
