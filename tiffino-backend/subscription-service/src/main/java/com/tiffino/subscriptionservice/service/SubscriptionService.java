package com.tiffino.subscriptionservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiffino.subscriptionservice.dto.CustomizationDTO;
import com.tiffino.subscriptionservice.dto.SubscriptionPlanDTO;
import com.tiffino.subscriptionservice.dto.UserSubscriptionDTO;
import com.tiffino.subscriptionservice.entity.SubscriptionPlan;
import com.tiffino.subscriptionservice.entity.UserSubscription;
import com.tiffino.subscriptionservice.enums.BillingCycle;
import com.tiffino.subscriptionservice.enums.SubscriptionStatus;
import com.tiffino.subscriptionservice.exception.SubscriptionNotFoundException;
import com.tiffino.subscriptionservice.exception.SubscriptionPlanNotFoundException;
import com.tiffino.subscriptionservice.repository.SubscriptionPlanRepository;
import com.tiffino.subscriptionservice.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final ObjectMapper objectMapper;

    public List<UserSubscriptionDTO> getAllSubscriptions() {
        return userSubscriptionRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    public List<UserSubscriptionDTO> getSubscriptionsByCycle(BillingCycle cycle) {
        return userSubscriptionRepository.findByBillingCycle(cycle).stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    public UserSubscriptionDTO getById(Long id) {
        return convertEntityToDto(userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription with ID " + id + " not found")));
    }

    public UserSubscriptionDTO createSubscription(Long userId, Long planId, CustomizationDTO input) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new SubscriptionPlanNotFoundException("Plan not found"));

        if (!Boolean.TRUE.equals(plan.getIsActive())) {
            throw new IllegalStateException("Subscription plan is inactive");
        }

        boolean alreadySubscribed = userSubscriptionRepository
                .existsByUserIdAndSubscriptionPlanIdAndStatus(userId, planId, SubscriptionStatus.ACTIVE);
        if (alreadySubscribed) {
            throw new IllegalStateException("User is already subscribed");
        }

        CustomizationDTO customizations = input != null ? input : new CustomizationDTO();

        String customizationsJson = serializeCustomizations(customizations);

        int durationWeeks = plan.getDurationInWeeks();
        BigDecimal price = plan.getPrice();
        boolean isInstallmentAllowed = durationWeeks > 4 && price.compareTo(BigDecimal.valueOf(1000)) > 0;

        int maxAllowedInstallments = (price.intValue() > 5000) ? 6 : (price.intValue() > 2000) ? 3 : 1;
        int totalInstallments = customizations.getTotalInstallments() != null
                ? customizations.getTotalInstallments() : 1;

        if (!isInstallmentAllowed && totalInstallments > 1) {
            throw new IllegalArgumentException("Installment payment not available for this plan");
        }

        if (totalInstallments > maxAllowedInstallments) {
            throw new IllegalArgumentException("Plan allows up to " + maxAllowedInstallments + " installments");
        }

        BigDecimal installmentAmount = null;
        Integer currentInstallment = null;
        LocalDate nextInstallmentDate = null;
        boolean isInstallmentActive = totalInstallments > 1;

        if (isInstallmentActive) {
            installmentAmount = price.divide(BigDecimal.valueOf(totalInstallments), 2, RoundingMode.HALF_UP);
            currentInstallment = 1;
            nextInstallmentDate = switch (customizations.getBillingCycle()) {
                case WEEKLY -> LocalDate.now().plusWeeks(1);
                case BIWEEKLY -> LocalDate.now().plusWeeks(2);
                case MONTHLY -> LocalDate.now().plusMonths(1);
                case QUARTERLY -> LocalDate.now().plusMonths(3);
                case YEARLY -> LocalDate.now().plusYears(1);
                default -> LocalDate.now().plusWeeks(1);
            };
        }

        UserSubscription subscription = UserSubscription.builder()
                .userId(userId)
                .subscriptionPlan(plan)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusWeeks(durationWeeks))
                .nextRenewalDate(LocalDate.now().plusWeeks(durationWeeks))
                .status(SubscriptionStatus.ACTIVE)
                .customizations(customizationsJson)
                .billingCycle(customizations.getBillingCycle())
                .autoRenew(customizations.getAutoRenew())
                .totalInstallments(isInstallmentActive ? totalInstallments : null)
                .currentInstallment(currentInstallment)
                .installmentAmount(installmentAmount)
                .nextInstallmentDate(nextInstallmentDate)
                .isInstallmentActive(isInstallmentActive)
                .build();

        return convertEntityToDto(userSubscriptionRepository.save(subscription));
    }

    public UserSubscriptionDTO updateCustomizations(Long subscriptionId, CustomizationDTO updates) {
        UserSubscription subscription = userSubscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        CustomizationDTO existing = deserializeCustomizations(subscription.getCustomizations());

        if (updates.getSpiceLevel() != null) existing.setSpiceLevel(updates.getSpiceLevel());
        if (updates.getPortionSize() != null) existing.setPortionSize(updates.getPortionSize());
        if (updates.getAdditionalRequests() != null) existing.setAdditionalRequests(updates.getAdditionalRequests());

        if (updates.getBillingCycle() != null) {
            existing.setBillingCycle(updates.getBillingCycle());
            subscription.setBillingCycle(updates.getBillingCycle());
        }

        if (updates.getAutoRenew() != null) {
            existing.setAutoRenew(updates.getAutoRenew());
            subscription.setAutoRenew(updates.getAutoRenew());
        }

        subscription.setCustomizations(serializeCustomizations(existing));
        return convertEntityToDto(userSubscriptionRepository.save(subscription));
    }

    public UserSubscriptionDTO pauseSubscription(Long id) {
        return updateStatus(id, SubscriptionStatus.PAUSED);
    }

    public UserSubscriptionDTO resumeSubscription(Long id) {
        return updateStatus(id, SubscriptionStatus.ACTIVE);
    }

    public UserSubscriptionDTO cancelSubscription(Long id) {
        return updateStatus(id, SubscriptionStatus.CANCELLED);
    }

    public UserSubscriptionDTO restoreSubscription(Long id) {
        UserSubscription subscription = userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        subscription.setDeletedAt(null);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        return convertEntityToDto(userSubscriptionRepository.save(subscription));
    }

    public List<UserSubscriptionDTO> getUserSubscriptions(Long userId) {
        return userSubscriptionRepository.findByUserId(userId).stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    // --- Helpers ---

    private UserSubscriptionDTO convertEntityToDto(UserSubscription subscription) {
        CustomizationDTO customizations = deserializeCustomizations(subscription.getCustomizations());
        customizations.setBillingCycle(subscription.getBillingCycle());
        customizations.setAutoRenew(subscription.getAutoRenew());

        return UserSubscriptionDTO.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .subscriptionPlan(convertPlanEntityToDto(subscription.getSubscriptionPlan()))
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .nextRenewalDate(subscription.getNextRenewalDate())
                .customizations(customizations)
                .build();
    }

    private SubscriptionPlanDTO convertPlanEntityToDto(SubscriptionPlan plan) {
        return SubscriptionPlanDTO.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .mealFrequency(plan.getMealFrequency())
                .mealType(plan.getMealType())
                .durationInWeeks(plan.getDurationInWeeks())
                .price(plan.getPrice())
                .build();
    }

    private String serializeCustomizations(CustomizationDTO dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize customizations", e);
            throw new RuntimeException("Error processing subscription customizations", e);
        }
    }

    private CustomizationDTO deserializeCustomizations(String json) {
        try {
            return (json != null && !json.isBlank())
                    ? objectMapper.readValue(json, CustomizationDTO.class)
                    : new CustomizationDTO();
        } catch (Exception e) {
            log.error("Failed to deserialize customizations", e);
            return new CustomizationDTO();
        }
    }

    private UserSubscriptionDTO updateStatus(Long id, SubscriptionStatus status) {
        UserSubscription sub = userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));
        sub.setStatus(status);
        return convertEntityToDto(userSubscriptionRepository.save(sub));
    }
}