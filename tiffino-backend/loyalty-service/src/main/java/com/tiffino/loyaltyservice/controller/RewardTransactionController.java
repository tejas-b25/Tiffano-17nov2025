package com.tiffino.loyaltyservice.controller;

import com.tiffino.loyaltyservice.dto.RewardTransactionDto;
import com.tiffino.loyaltyservice.service.RewardTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reward")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RewardTransactionController {

    private final RewardTransactionService rewardTransactionService;

    @PostMapping("/createReward")
    public ResponseEntity<RewardTransactionDto> create(@RequestBody RewardTransactionDto dto) {
        return ResponseEntity.ok(rewardTransactionService.createRewardTransaction(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RewardTransactionDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rewardTransactionService.getRewardTransactionById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RewardTransactionDto>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(rewardTransactionService.getRewardTransactionsByUserId(userId));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<RewardTransactionDto>> getAll() {
        return ResponseEntity.ok(rewardTransactionService.getAllTransactions());
    }

    @PostMapping("/referral/{userId}")
    public ResponseEntity<RewardTransactionDto> awardReferral(@PathVariable Long userId) {
        return ResponseEntity.ok(rewardTransactionService.awardReferralBonus(userId));
    }

    @PostMapping("/reverse/{orderId}")
    public ResponseEntity<String> reverseReward(@PathVariable Long orderId) {
        rewardTransactionService.reverseOrderReward(orderId);
        return ResponseEntity.ok("Reversed reward for order ID: " + orderId);
    }
}
