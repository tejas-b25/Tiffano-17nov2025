package com.tiffino.userservice.dto;

import com.tiffino.userservice.enums.*;
//import com.tiffino.user.enums.DietaryPreferences;
//import com.tiffino.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private Role role;
    private LocalDateTime dateJoined;
    private LocalDateTime lastLogin;
    private Boolean isActive;
    private String profileImageUrl;
    private DietaryPreferences dietaryPreferences;
    private AllergenPreferences allergenPreferences;

    private List<AddressResponse> addresses;
    private List<OrderResponse> orders;
    private List<GiftCardResponse> giftCards;
    private List<ReviewResponse> reviews;
    private List<UserSubscriptionResponse> subscriptions;
    private List<PaymentTransactionResponse> transactions;
    private LoyaltyPointResponse loyaltyPoint;
}
