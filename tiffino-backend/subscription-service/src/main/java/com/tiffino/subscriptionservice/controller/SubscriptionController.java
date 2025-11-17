package com.tiffino.subscriptionservice.controller;

import com.tiffino.subscriptionservice.dto.CustomizationDTO;
import com.tiffino.subscriptionservice.dto.UserSubscriptionDTO;
import com.tiffino.subscriptionservice.enums.BillingCycle;
import com.tiffino.subscriptionservice.scheduler.AutoRenewalScheduler;
import com.tiffino.subscriptionservice.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final AutoRenewalScheduler autoRenewalScheduler;

    /**
     * Fetches all subscriptions across all users.
     * Mainly for admin or analytics use.
     */
    @GetMapping("/getall")
    public ResponseEntity<List<UserSubscriptionDTO>> getAllUserSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    /**
     * Subscribes a user to a specific plan.
     * Accepts optional customizations for meal preferences, billing cycle, etc.
     */
    @PostMapping("/{userId}/subscribe/{planId}")
    public ResponseEntity<UserSubscriptionDTO> subscribeUser(
            @PathVariable Long userId,
            @PathVariable Long planId,
            @RequestBody(required = false) CustomizationDTO customizations
    ) {
        return ResponseEntity.ok(subscriptionService.createSubscription(userId, planId, customizations));
    }

    /**
     * Pauses an active subscription.
     */
    @PutMapping("/{subscriptionId}/pause")
    public ResponseEntity<UserSubscriptionDTO> pauseSubscription(@PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.pauseSubscription(subscriptionId));
    }

    /**
     * Resumes a previously paused subscription.
     * Reactivates service based on existing plan data.
     */
    @PutMapping("/{subscriptionId}/resume")
    public ResponseEntity<UserSubscriptionDTO> resumeSubscription(@PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.resumeSubscription(subscriptionId));
    }

    /**
     * Retrieves all subscriptions tied to a specific user.
     * Useful for user dashboards or account summaries.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSubscriptionDTO>> getUserSubscriptions(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(userId));
    }

    /**
     * Updates customization preferences for a given subscription.
     * Only applies selective non-null fields from the input.
     */
    @PutMapping("/customizations/{subscriptionId}")
    public ResponseEntity<UserSubscriptionDTO> updateCustomizations(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody CustomizationDTO customizations) {
        return ResponseEntity.ok(subscriptionService.updateCustomizations(subscriptionId, customizations));
    }

    /**
     * Cancels an active subscription.
     * Marks the status as CANCELLED but does not delete the record.
     */
    @PutMapping("/{subscriptionId}/cancel")
    public ResponseEntity<UserSubscriptionDTO> cancelSubscription(@PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(subscriptionId));
    }

    /**
     * Get all users subscribed to a specific billing cycle (DAILY, WEEKLY, MONTHLY, etc.)
     */
    @GetMapping("/cycle/{billingCycle}")
    public ResponseEntity<List<UserSubscriptionDTO>> getByBillingCycle(@PathVariable BillingCycle billingCycle) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByCycle(billingCycle));
    }

    /**
     * Manually triggers the auto-renewal logic for subscriptions that are due.
     * Typically invoked by schedulers but exposed for testing/demo.
     */
    @GetMapping("/run-renewal-job")
    public ResponseEntity<String> simulateRenewals() {
        autoRenewalScheduler.renewEligibleSubscriptions();
        return ResponseEntity.ok("Auto-renewal job executed manually.");
    }
}