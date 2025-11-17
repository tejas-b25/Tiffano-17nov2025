package com.tiffino.userservice.mapper;

import com.tiffino.userservice.dto.UserResponse;
import com.tiffino.userservice.entity.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .dateJoined(user.getDateJoined())
                .lastLogin(user.getLastLogin())
                .isActive(user.getIsActive())
                .profileImageUrl(user.getProfileImageUrl())
                .dietaryPreferences(user.getDietaryPreferences())
                .allergenPreferences(user.getAllergenPreferences())

                // nested mapping
                .addresses(user.getAddresses()
                        .stream()
                        .map(AddressMapper::toResponse)
                        .collect(Collectors.toList()))
//                .orders(user.getOrders()
//                        .stream()
//                        .map(OrderMapper::toResponse)
//                        .collect(Collectors.toList()))
//                .giftCards(user.getGiftCards()
//                        .stream()
//                        .map(GiftCardMapper::toResponse)
//                        .collect(Collectors.toList()))
//                .reviews(user.getReviews()
//                        .stream()
//                        .map(ReviewMapper::toResponse)
//                        .collect(Collectors.toList()))
//                .subscriptions(user.getSubscriptions()
//                        .stream()
//                        .map(UserSubscriptionMapper::toResponse)
//                        .collect(Collectors.toList()))
//                .transactions(user.getTransactions()
//                        .stream()
//                        .map(PaymentTransactionMapper::toResponse)
//                        .collect(Collectors.toList()))
//                .loyaltyPoint(LoyaltyPointMapper.toResponse(user.getLoyaltyPoint()))

                .build();
    }
}
