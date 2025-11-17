package com.tiffino.subscriptionservice.controller;

import com.tiffino.subscriptionservice.dto.SubscriptionPlanDTO;
import com.tiffino.subscriptionservice.entity.SubscriptionPlan;
import com.tiffino.subscriptionservice.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
@CrossOrigin("*")


public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    /** Get all active subscription plans */
    @GetMapping("/getall")
    public ResponseEntity<List<SubscriptionPlan>> getAllActivePlans() {
        return ResponseEntity.ok(subscriptionPlanService.getAllPlans());
    }

    /**  Get a specific plan by ID */
    @GetMapping("/{planId}")
    public ResponseEntity<SubscriptionPlan> getPlanById(@PathVariable Long planId) {
        return ResponseEntity.ok(subscriptionPlanService.getPlanById(planId));
    }

    @GetMapping("/search-plan")
    public ResponseEntity<List<SubscriptionPlan>> getPlansByName(@RequestParam String name) {
        return ResponseEntity.ok(subscriptionPlanService.getPlansByName(name));
    }

    @GetMapping("/filter-by-plan-status")
    public ResponseEntity<List<SubscriptionPlan>> getPlansByStatus(
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(subscriptionPlanService.getPlansByStatus(active));
    }

    /**  Create a new subscription plan */
    @PostMapping("/create")
    public ResponseEntity<SubscriptionPlan> createPlan(@Valid @RequestBody SubscriptionPlanDTO planDTO) {
        return ResponseEntity.ok(subscriptionPlanService.createPlan(planDTO));
    }

    /**  Update an existing subscription plan */
    @PutMapping("/update/{planId}")
    public ResponseEntity<SubscriptionPlan> updatePlan(
            @PathVariable Long planId,
            @Valid @RequestBody SubscriptionPlanDTO updatedPlan) {
        return ResponseEntity.ok(subscriptionPlanService.updatePlan(planId, updatedPlan));
    }

    /**  Softly delete a subscription plan (mark as inactive instead of permanent deletion) */
    @PutMapping("/deactivate/{planId}")
    public ResponseEntity<SubscriptionPlan> deactivatePlan(@PathVariable Long planId) {
        return ResponseEntity.ok(subscriptionPlanService.deactivatePlan(planId));
    }

    /**  Permanently delete a subscription plan */
    @DeleteMapping("/delete/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long planId) {
        subscriptionPlanService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }
}