package com.tiffino.paymentservice.controller;

import com.tiffino.paymentservice.dto.PaymentTransactionDTO;
import com.tiffino.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentTransactionController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentTransactionDTO> initiatePayment(@RequestBody PaymentTransactionDTO dto) {
        return ResponseEntity.ok(paymentService.initiatePayment(dto));
    }
    @PutMapping("/confirm/order/{orderId}")
    public ResponseEntity<PaymentTransactionDTO> confirmByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.confirmTransactionByOrderId(orderId));
    }

    @PutMapping("/cancel/order/{orderId}")
    public ResponseEntity<PaymentTransactionDTO> cancelByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.cancelTransactionByOrderId(orderId));
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/retry/{id}")
    public ResponseEntity<PaymentTransactionDTO> retry(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.retryPayment(id));
    }

    @PostMapping("/refund/{orderId}")
    public ResponseEntity<PaymentTransactionDTO> refund(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.refundPayment(orderId));
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<PaymentTransactionDTO>> getTransactionsByUserId(@PathVariable Long userId) {
        List<PaymentTransactionDTO> dtoList = paymentService.getAllTransactionsByUserId(userId);
        return ResponseEntity.ok(dtoList);
    }

}
