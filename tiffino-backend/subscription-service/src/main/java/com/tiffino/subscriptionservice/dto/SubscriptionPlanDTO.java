package com.tiffino.subscriptionservice.dto;

import com.tiffino.subscriptionservice.enums.MealType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanDTO {

//    @NotNull(message = "Plan ID cannot be null")
    private Long id;

    @NotBlank(message = "Plan name cannot be empty")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 255, message = "Description must be within 255 characters")
    private String description;

    @NotBlank(message = "Meal frequency cannot be empty")
    private String mealFrequency;

    @NotNull(message = "Meal type must be specified")
    private MealType mealType;

    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be at least 1 week")
    private Integer durationInWeeks;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "1.0", message = "Price must be greater than 1")
    private BigDecimal price;
}