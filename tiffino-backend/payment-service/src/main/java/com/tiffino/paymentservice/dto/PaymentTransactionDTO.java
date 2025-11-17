package com.tiffino.paymentservice.dto;

import com.tiffino.paymentservice.entity.PaymentTransaction;
import com.tiffino.paymentservice.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionDTO {
    private Long id;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private String status;
    private LocalDateTime transactionDate;

    private String voucherCode;
    private String giftCardCode;
    private Long cuisineId;

    public PaymentTransactionDTO(PaymentTransaction entity) {
        this.id = entity.getId();
        this.orderId = entity.getOrderId();
        this.userId = entity.getUserId();
        this.amount = entity.getAmount();
        this.paymentMethod = entity.getPaymentMethod();
        this.transactionId = entity.getTransactionId();
        this.status = entity.getStatus();
        this.transactionDate = entity.getTransactionDate();
        this.voucherCode = entity.getVoucherCode();
        this.giftCardCode = entity.getGiftCardCode();
        this.cuisineId = entity.getCuisineId();
    }


}
