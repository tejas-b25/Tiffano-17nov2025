package com.tiffino.menuservice.dto;

import com.tiffino.menuservice.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuisineDTO {

    private Long id;
    private String name;
    private String imageUrl;

    private Long stateId; // ✅ Added for DTO mapping

    private MealType meal_type;
    private int price_per_meal;
    private int price_per_week;
    private int price_per_month;
    private String currency;
    private boolean is_offer_active;
    private boolean is_available;
    private List<AvailableDay> available_days;
    private int calories;
    private String protein;
    private String carbs;
    private String fats;
    private String fiber;
    private AllergenPreference allergen_preferences;
    private DietaryPreference dietary_preferences;
    private String preparation_time;
    private SpiceLevel spice_level;
}
