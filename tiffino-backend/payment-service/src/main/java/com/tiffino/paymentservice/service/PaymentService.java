package com.tiffino.paymentservice.service;

import com.tiffino.paymentservice.dto.PaymentTransactionDTO;

import java.util.List;

public interface PaymentService {
    PaymentTransactionDTO initiatePayment(PaymentTransactionDTO dto);
    PaymentTransactionDTO retryPayment(String transactionId);
    PaymentTransactionDTO refundPayment(Long orderId);
    PaymentTransactionDTO confirmTransactionByOrderId(Long orderId);
    PaymentTransactionDTO cancelTransactionByOrderId(Long orderId);
    List<PaymentTransactionDTO> getAllTransactionsByUserId(Long userId);


}
