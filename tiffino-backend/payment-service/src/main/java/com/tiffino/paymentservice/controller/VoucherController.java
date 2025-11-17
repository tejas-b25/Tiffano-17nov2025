package com.tiffino.paymentservice.controller;

import com.tiffino.paymentservice.dto.VoucherDTO;
import com.tiffino.paymentservice.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping("/create")
    public ResponseEntity<VoucherDTO> create(@RequestBody VoucherDTO dto) {
        return ResponseEntity.ok(voucherService.createVoucher(dto));
    }


    @GetMapping("/getall")
    public ResponseEntity<List<VoucherDTO>> getAllVouchers() {
        return ResponseEntity.ok(voucherService.getAllVouchers());
    }

    @GetMapping("/byCode/{code}")
    public ResponseEntity<VoucherDTO> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(voucherService.getVoucherByCode(code));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateVoucher(
            @RequestParam String code,
            @RequestParam BigDecimal orderAmount,
            @RequestParam(required = false) Long cuisineId) {
        boolean isValid = voucherService.validateVoucher(code, orderAmount, cuisineId);
        return ResponseEntity.ok(isValid);
    }
}
