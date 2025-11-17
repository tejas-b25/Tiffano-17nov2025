package com.tiffino.subscriptionservice.service;

import com.tiffino.subscriptionservice.dto.SubscriptionPlanDTO;
import com.tiffino.subscriptionservice.entity.SubscriptionPlan;
import com.tiffino.subscriptionservice.enums.MealType;
import com.tiffino.subscriptionservice.exception.SubscriptionPlanNotFoundException;
import com.tiffino.subscriptionservice.repository.SubscriptionPlanRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public List<SubscriptionPlan> getAllPlans() {
        return subscriptionPlanRepository.findAll();
    }

    public List<SubscriptionPlan> getPlansByName(String name) {
        return subscriptionPlanRepository.findByNameContainingIgnoreCase(name);
    }

    public SubscriptionPlan getPlanById(Long planId) {
        return subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new SubscriptionPlanNotFoundException(
                        "Subscription Plan with ID " + planId + " not found"));
    }

    public List<SubscriptionPlan> getPlansByStatus(Boolean status) {
        return subscriptionPlanRepository.findByStatus(status);
    }

    public List<SubscriptionPlan> getPlansByMealType(MealType mealType) {
        return subscriptionPlanRepository.findByMealType(mealType);
    }

    public SubscriptionPlan createPlan(SubscriptionPlanDTO planDTO) {
        SubscriptionPlan plan = SubscriptionPlan.builder()
                .name(planDTO.getName())
                .description(planDTO.getDescription())
                .mealFrequency(planDTO.getMealFrequency())
                .durationInWeeks(planDTO.getDurationInWeeks())
                .price(planDTO.getPrice())
                .mealType(planDTO.getMealType()) // 👈 included here
                .isActive(true)
                .build();

        return subscriptionPlanRepository.save(plan);
    }

    public SubscriptionPlan updatePlan(Long planId, @Valid SubscriptionPlanDTO updatedPlan) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new SubscriptionPlanNotFoundException(
                        "Subscription Plan with ID " + planId + " not found"));

        plan.setName(updatedPlan.getName());
        plan.setDescription(updatedPlan.getDescription());
        plan.setPrice(updatedPlan.getPrice());
        plan.setDurationInWeeks(updatedPlan.getDurationInWeeks());
        plan.setMealFrequency(updatedPlan.getMealFrequency());
        plan.setMealType(updatedPlan.getMealType()); // 👈 included here

        return subscriptionPlanRepository.save(plan);
    }

    public SubscriptionPlan deactivatePlan(Long planId) {
        SubscriptionPlan plan = getPlanById(planId);
        plan.setActive(false);
        return subscriptionPlanRepository.save(plan);
    }

    public void deletePlan(Long planId) {
        if (!subscriptionPlanRepository.existsById(planId)) {
            throw new SubscriptionPlanNotFoundException(
                    "Subscription Plan with ID " + planId + " not found");
        }
        subscriptionPlanRepository.deleteById(planId);
    }
}