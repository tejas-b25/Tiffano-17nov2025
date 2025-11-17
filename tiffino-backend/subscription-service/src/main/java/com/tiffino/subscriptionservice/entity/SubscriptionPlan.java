package com.tiffino.subscriptionservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tiffino.subscriptionservice.enums.MealType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "subscription_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Plan name cannot be Empty")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 255, message = "Description must be within 255 characters")
    private String description;

    @NotBlank(message = "Meal Frequency cannot be empty")
    private String mealFrequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    @Min(value = 1, message = "Duration must be at least 1 week")
    @Column(nullable = false)
    private Integer durationInWeeks;

    @DecimalMin(value = "1.0", message = "Price must be at least 1")
    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserSubscription> userSubscriptions;

    public void setActive(Boolean active) {
        this.isActive = active;
    }
}