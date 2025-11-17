package com.tiffino.paymentservice.service;

import com.tiffino.paymentservice.dto.VoucherDTO;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {
    VoucherDTO createVoucher(VoucherDTO dto);
    VoucherDTO getVoucherByCode(String code);
    boolean validateVoucher(String code, BigDecimal orderAmount, Long cuisineId);
    void incrementUsage(String code);
    List<VoucherDTO> getAllVouchers();
}
