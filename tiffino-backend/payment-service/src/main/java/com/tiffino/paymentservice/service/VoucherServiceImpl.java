package com.tiffino.paymentservice.service;

import com.tiffino.paymentservice.client.CuisineClient;
import com.tiffino.paymentservice.dto.CuisineDTO;
import com.tiffino.paymentservice.dto.VoucherDTO;
import com.tiffino.paymentservice.entity.Voucher;
import com.tiffino.paymentservice.execption.VoucherValidationException;
import com.tiffino.paymentservice.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final CuisineClient cuisineClient;

    @Override
    public VoucherDTO createVoucher(VoucherDTO dto) {
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(dto, voucher);
        voucher.setCurrentUses(0);
        voucher.setIsActive(true);
        voucherRepository.save(voucher);
        dto.setId(voucher.getId());
        return dto;
    }

    @Override

    public List<VoucherDTO> getAllVouchers() {

        List<Voucher> vouchers = voucherRepository.findAll();

        return vouchers.stream().map(voucher -> {

            VoucherDTO dto = new VoucherDTO();

            BeanUtils.copyProperties(voucher, dto);

            return dto;

        }).collect(Collectors.toList());

    }


    @Override
    public VoucherDTO getVoucherByCode(String code) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("Voucher with code '" + code + "' not found"));
        VoucherDTO dto = new VoucherDTO();
        BeanUtils.copyProperties(voucher, dto);
        return dto;
    }

    @Override
    public boolean validateVoucher(String code, BigDecimal orderAmount, Long cuisineId) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new VoucherValidationException("Invalid voucher code: " + code));

        if (!Boolean.TRUE.equals(voucher.getIsActive()))
            throw new VoucherValidationException("Voucher '" + code + "' is inactive");

        if (voucher.getExpiryDate().isBefore(LocalDate.now()))
            throw new VoucherValidationException("Voucher '" + code + "' has expired on " + voucher.getExpiryDate());

        if (voucher.getUsageLimit() != null && voucher.getCurrentUses() >= voucher.getUsageLimit())
            throw new VoucherValidationException("Voucher usage limit exceeded");

        if (voucher.getMinOrderAmount() != null && orderAmount.compareTo(voucher.getMinOrderAmount()) < 0)
            throw new VoucherValidationException("Order amount is below minimum required for this voucher");

        if (voucher.getApplicableCuisineId() != null) {
            CuisineDTO cuisine = cuisineClient.getCuisineById(cuisineId);
            if (cuisine == null || !Objects.equals(voucher.getApplicableCuisineId(), cuisine.getId())) {
                throw new VoucherValidationException("Voucher not applicable to selected cuisine");
            }
        }

        return true;
    }


    @Override
    public void incrementUsage(String code) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        voucher.setCurrentUses(voucher.getCurrentUses() + 1);
        voucherRepository.save(voucher);
    }
}