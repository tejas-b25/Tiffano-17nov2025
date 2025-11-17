package com.tiffino.subscriptionservice.repository;

import com.tiffino.subscriptionservice.entity.SubscriptionPlan;
import com.tiffino.subscriptionservice.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    @Query("SELECT p FROM SubscriptionPlan p WHERE p.isActive = true")
    List<SubscriptionPlan> findByIsActiveTrue();

    @Query("SELECT p FROM SubscriptionPlan p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<SubscriptionPlan> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT p FROM SubscriptionPlan p WHERE (:status IS NULL OR p.isActive = :status)")
    List<SubscriptionPlan> findByStatus(@Param("status") Boolean status);

    List<SubscriptionPlan> findByMealType(MealType mealType);
}