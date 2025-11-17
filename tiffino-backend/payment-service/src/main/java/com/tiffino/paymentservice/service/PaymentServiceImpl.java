package com.tiffino.paymentservice.service;

import com.tiffino.paymentservice.dto.*;
import com.tiffino.paymentservice.entity.PaymentTransaction;
import com.tiffino.paymentservice.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentTransactionRepository transactionRepo;
    private final VoucherService voucherService;
    private final GiftCardService giftCardService;

    @Override
    public PaymentTransactionDTO initiatePayment(PaymentTransactionDTO dto) {
        BigDecimal originalAmount = dto.getAmount();

        // Apply Voucher
        if (dto.getVoucherCode() != null) {
            boolean valid = voucherService.validateVoucher(dto.getVoucherCode(), originalAmount, dto.getCuisineId());
            if (!valid) throw new IllegalArgumentException("Invalid voucher");

            VoucherDTO voucher = voucherService.getVoucherByCode(dto.getVoucherCode());
            BigDecimal discount = calculateDiscount(voucher, originalAmount);
            originalAmount = originalAmount.subtract(discount);
            voucherService.incrementUsage(dto.getVoucherCode());
        }

        // Apply GiftCard
        if (dto.getGiftCardCode() != null) {
            GiftCardDTO giftCard = giftCardService.redeemGiftCard(dto.getGiftCardCode(), originalAmount);
            BigDecimal used = giftCard.getInitialAmount();//.subtract(giftCard.getCurrentBalance());
            originalAmount = originalAmount.subtract(used);
        }

        PaymentTransaction txn = PaymentTransaction.builder()
                .orderId(dto.getOrderId())
                .userId(dto.getUserId())
                .amount(originalAmount)
                .paymentMethod(dto.getPaymentMethod())
                .transactionId(UUID.randomUUID().toString())
               //.status(Math.random() < 0.8 ? "PENDING" : "FAILED")
               .status("FAILED")
                .transactionDate(LocalDateTime.now())
                .voucherCode(dto.getVoucherCode())
                .giftCardCode(dto.getGiftCardCode())
                .cuisineId(dto.getCuisineId())
                .build();

        transactionRepo.save(txn);
        return convert(txn);

    }

    private BigDecimal calculateDiscount(VoucherDTO voucher, BigDecimal orderAmount) {
        if ("PERCENTAGE".equalsIgnoreCase(voucher.getDiscountType())) {
            BigDecimal discount = orderAmount.multiply(voucher.getDiscountValue().divide(BigDecimal.valueOf(100)));
            return (voucher.getMaxDiscountAmount() != null) ?
                    discount.min(voucher.getMaxDiscountAmount()) : discount;
        } else {
            return voucher.getDiscountValue();
        }
    }
    @Override
    public PaymentTransactionDTO confirmTransactionByOrderId(Long orderId) {
        PaymentTransaction txn = transactionRepo.findByOrderId(orderId)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found for orderId: " + orderId));

        if (!"PENDING".equalsIgnoreCase(txn.getStatus())) {
            throw new IllegalStateException("Only PENDING transactions can be confirmed.");
        }

        txn.setStatus("SUCCESS");
        txn.setTransactionDate(LocalDateTime.now());

        PaymentTransaction updated = transactionRepo.save(txn);
        return new PaymentTransactionDTO(updated);
    }

    @Override
    public PaymentTransactionDTO cancelTransactionByOrderId(Long orderId) {
        PaymentTransaction txn = transactionRepo.findByOrderId(orderId)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found for orderId: " + orderId));

        if (!"PENDING".equalsIgnoreCase(txn.getStatus())) {
            throw new IllegalStateException("Only PENDING transactions can be cancelled.");
        }

        txn.setStatus("CANCELLED");
        txn.setTransactionDate(LocalDateTime.now());

        PaymentTransaction updated = transactionRepo.save(txn);
        return new PaymentTransactionDTO(updated);
    }


    @Override
    public PaymentTransactionDTO retryPayment(String transactionId) {
        PaymentTransaction txn = transactionRepo.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!"FAILED".equals(txn.getStatus()))
            throw new IllegalStateException("Only FAILED transactions can be retried.");

        txn.setStatus("PENDING");
        txn.setTransactionId(UUID.randomUUID().toString());
        txn.setTransactionDate(LocalDateTime.now());

        transactionRepo.save(txn);
        return convert(txn);
    }

    @Override
    public PaymentTransactionDTO refundPayment(Long orderId) {
        PaymentTransaction txn = transactionRepo.findByOrderId(Long.valueOf(orderId))
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        txn.setStatus("REFUNDED");
        transactionRepo.save(txn);
        return convert(txn);
    }

    private PaymentTransactionDTO convert(PaymentTransaction txn) {
        return PaymentTransactionDTO.builder()
                .id(txn.getId())
                .orderId(txn.getOrderId())
                .userId(txn.getUserId())
                .amount(txn.getAmount())
                .paymentMethod(txn.getPaymentMethod())
                .transactionId(txn.getTransactionId())
                .status(txn.getStatus())
                .transactionDate(txn.getTransactionDate())
                .voucherCode(txn.getVoucherCode())
                .giftCardCode(txn.getGiftCardCode())
                .cuisineId(txn.getCuisineId())
                .build();
    }

    @Override
    public List<PaymentTransactionDTO> getAllTransactionsByUserId(Long userId) {
        List<PaymentTransaction> transactions = transactionRepo.findByUserId(userId);
        return transactions.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private PaymentTransactionDTO convertToDTO(PaymentTransaction transaction) {
        return PaymentTransactionDTO.builder()
                .id(transaction.getId())
                .orderId(transaction.getOrderId())
                .userId(transaction.getUserId())
                .amount(transaction.getAmount())
                .paymentMethod(transaction.getPaymentMethod())
                .transactionId(transaction.getTransactionId())
                .status(transaction.getStatus())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }

}
