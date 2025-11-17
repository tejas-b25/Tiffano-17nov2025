package com.tiffino.subscriptionservice.scheduler;

import com.tiffino.subscriptionservice.entity.UserSubscription;
import com.tiffino.subscriptionservice.enums.BillingCycle;
import com.tiffino.subscriptionservice.enums.SubscriptionStatus;
import com.tiffino.subscriptionservice.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoRenewalScheduler {

    private final UserSubscriptionRepository userSubscriptionRepository;

    @Scheduled(cron = "0 0 3 * * *") // Runs every day at 3 AM
    public void renewEligibleSubscriptions() {
        List<UserSubscription> subscriptions = userSubscriptionRepository
                .findByAutoRenewTrueAndNextRenewalDateAndStatus(LocalDate.now(), SubscriptionStatus.ACTIVE);

        for (UserSubscription subscription : subscriptions) {
            log.info("Processing renewal for subscription ID {}", subscription.getId());

            // 🔄 Installment logic
            if (Boolean.TRUE.equals(subscription.getIsInstallmentActive())) {
                int current = subscription.getCurrentInstallment();
                int total = subscription.getTotalInstallments();

                log.info("Installment {}/{} for subscription ID {}", current, total, subscription.getId());
                // 💳 Simulate payment with installmentAmount (e.g., hit Razorpay/Stripe)

                subscription.setCurrentInstallment(current + 1);

                if (current + 1 > total) {
                    subscription.setIsInstallmentActive(false);
                    log.info("Installment completed for subscription ID {}", subscription.getId());
                }

                // 🔁 Update next installment date (adjust based on your plan frequency)
                subscription.setNextInstallmentDate(subscription.getNextInstallmentDate().plusMonths(1));
            }

            // 🔄 Standard full plan renewal
            else {
                subscription.setStartDate(LocalDate.now());
                subscription.setNextRenewalDate(calculateNext(subscription.getBillingCycle()));
                subscription.setStatus(SubscriptionStatus.ACTIVE);
                log.info("Subscription fully renewed: ID {}", subscription.getId());
            }

            userSubscriptionRepository.save(subscription);
        }
    }

    private LocalDate calculateNext(BillingCycle billingCycle) {
        return switch (billingCycle) {
            case DAILY     -> LocalDate.now().plusDays(1);
            case WEEKLY    -> LocalDate.now().plusWeeks(1);
            case BIWEEKLY  -> LocalDate.now().plusWeeks(2);
            case MONTHLY   -> LocalDate.now().plusMonths(1);
            case QUARTERLY -> LocalDate.now().plusMonths(3);
            case YEARLY    -> LocalDate.now().plusYears(1);
            default        -> LocalDate.now();
        };
    }


    @Scheduled(cron = "0 30 3 * * *") // Runs daily at 3:30 AM
    public void expireStaleSubscriptions() {
        List<UserSubscription> expiredSubscriptions = userSubscriptionRepository
                .findByStatusAndEndDateBefore(SubscriptionStatus.ACTIVE, LocalDate.now());

        if (expiredSubscriptions.isEmpty()) {
            log.info("No subscriptions to expire today.");
            return;
        }

        for (UserSubscription sub : expiredSubscriptions) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
            log.info("Marked subscription ID {} as EXPIRED (ended on {})", sub.getId(), sub.getEndDate());
        }

        userSubscriptionRepository.saveAll(expiredSubscriptions);
    }


}