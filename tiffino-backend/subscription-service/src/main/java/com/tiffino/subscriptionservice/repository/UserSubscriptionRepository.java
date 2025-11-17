package com.tiffino.subscriptionservice.repository;

import com.tiffino.subscriptionservice.entity.UserSubscription;
import com.tiffino.subscriptionservice.enums.BillingCycle;
import com.tiffino.subscriptionservice.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    List<UserSubscription> findByUserId(Long userId);
    List<UserSubscription> findByStatus(SubscriptionStatus status);

    @Query("SELECT s FROM UserSubscription s WHERE s.deletedAt IS NULL AND s.status != 'CANCELED'")
    List<UserSubscription> findActiveSubscriptions();

    List<UserSubscription> findByAutoRenewTrueAndNextRenewalDateAndStatus(
            LocalDate date,
            SubscriptionStatus status
    );

    boolean existsByUserIdAndSubscriptionPlanIdAndStatus(Long userId, Long subscriptionPlanId, SubscriptionStatus status);

    List<UserSubscription> findByStatusAndEndDateBefore(SubscriptionStatus status, LocalDate date);

    List<UserSubscription> findByBillingCycle(BillingCycle billingCycle);
}