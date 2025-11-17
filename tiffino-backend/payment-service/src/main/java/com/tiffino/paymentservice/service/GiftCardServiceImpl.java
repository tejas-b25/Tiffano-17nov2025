package com.tiffino.paymentservice.service;

import com.tiffino.paymentservice.client.UserClient;
import com.tiffino.paymentservice.client.UserDTO;
import com.tiffino.paymentservice.dto.GiftCardDTO;
import com.tiffino.paymentservice.entity.GiftCard;
import com.tiffino.paymentservice.repository.GiftCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCardServiceImpl implements GiftCardService {

    @Autowired
    private final GiftCardRepository giftCardRepository;
    @Autowired
    private UserClient userClient;

    @Override
    public GiftCardDTO issueGiftCard(GiftCardDTO dto) {
        GiftCard card = new GiftCard();
        BeanUtils.copyProperties(dto, card);
        card.setStatus("ACTIVE");
        card.setCurrentBalance(card.getInitialAmount());
        if (dto.getIssuedToUserId() != null) {
            try {
                UserDTO user = userClient.getUserById(dto.getIssuedToUserId());
                System.out.println("Fetched user = " + user);
                card.setIssuedToEmail(user.getEmail());
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch user info for ID: " + dto.getIssuedToUserId());
            }
        }
//        giftCardRepository.save(card);
//        dto.setId(card.getId());
//        dto.setIssuedToEmail(card.getIssuedToEmail());
//        return dto;
        card = giftCardRepository.save(card);
        GiftCardDTO updatedDTO = new GiftCardDTO();
        BeanUtils.copyProperties(card, updatedDTO);
        return updatedDTO;
    }

    @Override
    public GiftCardDTO redeemGiftCard(String code, BigDecimal requestedAmount) {
        GiftCard card = giftCardRepository.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("GiftCard with code '" + code + "' not found"));
        if (!"ACTIVE".equalsIgnoreCase(card.getStatus()))
            throw new IllegalStateException("Gift card is not active");

        if (card.getExpiryDate().isBefore(LocalDate.now()))
            throw new IllegalStateException("Gift card expired");
        BigDecimal amountToUse = card.getCurrentBalance().min(requestedAmount);

//        if (card.getCurrentBalance().compareTo(amount) < 0)
//            throw new IllegalStateException("Insufficient gift card balance");

        card.setCurrentBalance(card.getCurrentBalance().subtract(amountToUse));
        if (card.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0)
            card.setStatus("REDEEMED");

        giftCardRepository.save(card);

        GiftCardDTO dto = new GiftCardDTO();
        BeanUtils.copyProperties(card, dto);
        dto.setInitialAmount(amountToUse);
        return dto;
    }

    @Override
    public List<GiftCardDTO> getGiftCardsByUser(Long userId) {
        List<GiftCardDTO> giftCards = giftCardRepository.findAll().stream()
                .filter(card -> Objects.equals(card.getIssuedToUserId(), userId))
                .map(card -> {
                    GiftCardDTO dto = new GiftCardDTO();
                    BeanUtils.copyProperties(card, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        if (giftCards.isEmpty()) {
            throw new NoSuchElementException("No gift cards issued to user ID: " + userId);
        }

        return giftCards;
    }

}
