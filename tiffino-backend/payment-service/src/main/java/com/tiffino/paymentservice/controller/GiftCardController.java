package com.tiffino.paymentservice.controller;

import com.tiffino.paymentservice.dto.GiftCardDTO;
import com.tiffino.paymentservice.service.GiftCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/giftcards")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GiftCardController {

    private final GiftCardService giftCardService;

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/run")
    public String Welcome() {
        return "Welcome to Payment Service";
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/issue")
    public ResponseEntity<GiftCardDTO> issue(@RequestBody GiftCardDTO dto) {
        return ResponseEntity.ok(giftCardService.issueGiftCard(dto));
    }

    @PostMapping("/redeem")
    public ResponseEntity<GiftCardDTO> redeem(
            @RequestParam String code,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(giftCardService.redeemGiftCard(code, amount));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GiftCardDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(giftCardService.getGiftCardsByUser(userId));
    }
}
