package com.tiffino.loyaltyservice.controller;

import com.tiffino.loyaltyservice.dto.LoyaltyPointDto;
import com.tiffino.loyaltyservice.service.LoyaltyPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LoyaltyPointController {

    private final LoyaltyPointService loyaltyPointService;

    @PostMapping("/createPoint")
    public ResponseEntity<LoyaltyPointDto> create(@RequestBody LoyaltyPointDto dto) {
        return ResponseEntity.ok(loyaltyPointService.createLoyaltyPoint(dto));
    }

//    @GetMapping("/{userId}")
    @GetMapping("/{userId}")
    public ResponseEntity<LoyaltyPointDto> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(loyaltyPointService.getLoyaltyPointByUserId(userId));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<LoyaltyPointDto>> getAll() {
        return ResponseEntity.ok(loyaltyPointService.getAllLoyaltyPoints());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<LoyaltyPointDto> update(@PathVariable Long userId, @RequestBody LoyaltyPointDto dto) {
        return ResponseEntity.ok(loyaltyPointService.updateLoyaltyPoint(userId, dto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable Long userId) {
        String message = loyaltyPointService.deleteLoyaltyPoint(userId);
        return ResponseEntity.ok(message);
    }

}
