package com.tiffino.menuservice.entity;

import com.tiffino.menuservice.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @Enumerated(EnumType.STRING)
    private MealType meal_type; // Enum

    private int price_per_meal;
    private int price_per_week;
    private int price_per_month;
    private String currency;
    private boolean is_offer_active;
    private boolean is_available;

    @ElementCollection(targetClass = AvailableDay.class)
    @Enumerated(EnumType.STRING)
    private List<AvailableDay> available_days; // ✅ Multiple days

    private int calories;
    private String protein;
    private String carbs;
    private String fats;
    private String fiber;

    @Enumerated(EnumType.STRING)
    private AllergenPreference allergen_preferences; // Enum

    @Enumerated(EnumType.STRING)
    private DietaryPreference dietary_preferences; // Enum

    private String preparation_time;

    @Enumerated(EnumType.STRING)
    private SpiceLevel spice_level; // Enum
}
