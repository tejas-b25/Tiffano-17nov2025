package com.tiffino.userservice.service;

import com.tiffino.userservice.client.*;
import com.tiffino.userservice.dto.*;
import com.tiffino.userservice.entity.Address;
import com.tiffino.userservice.entity.User;
import com.tiffino.userservice.enums.Role;
import com.tiffino.userservice.exceptions.ResourceAlreadyExistsException;
import com.tiffino.userservice.exceptions.ResourceNotFoundException;
import com.tiffino.userservice.repository.UserRepository;
import com.tiffino.userservice.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // Feign Clients
    private final OrderClient orderClient;
    private final PaymentClient paymentClient;
    private final GiftCardClient giftCardClient;
    private final UserSubscriptionClient subscriptionClient;
    private final ReviewClient reviewClient;
    private final LoyaltyPointClient loyaltyPointClient;

    @Override
    @Transactional
    @PreAuthorize("isAnonymous() or hasAnyRole('ADMIN','SUBADMIN','CUSTOMER','DELIVERY_PARTNER')")
    public UserResponse createUser(UserRequest request) {
        if (request == null || request.getEmail() == null) {
            throw new IllegalArgumentException("UserRequest or email must not be null");
        }

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new ResourceAlreadyExistsException("User already exists with email: " + request.getEmail());
        });

        User user = mapToUserEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDateJoined(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setIsActive(Boolean.TRUE.equals(request.getIsActive()));

        List<Address> addressList = convertToAddressEntities(request.getAddresses(), user);
        user.setAddresses(addressList);

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBADMIN','CUSTOMER','DELIVERY_PARTNER')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBADMIN','CUSTOMER','DELIVERY_PARTNER')")
    public UserResponse getUser(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    //@PreAuthorize("hasAnyRole('ADMIN', 'SUBADMIN','CUSTOMER','DELIVERY_PARTNER')")
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        updateUserData(user, request);
        List<Address> updatedAddresses = convertToAddressEntities(request.getAddresses(), user);
        user.getAddresses().clear();
        user.getAddresses().addAll(updatedAddresses);

        return mapToUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBADMIN','CUSTOMER','DELIVERY_PARTNER')")
    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBADMIN','CUSTOMER','DELIVERY_PARTNER')")
    public UserResponse getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with phone number: " + phoneNumber));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBADMIN','CUSTOMER','DELIVERY_PARTNER')")
    public String deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
        return "User deleted successfully with id: " + id;
    }

    @Override
    @PreAuthorize("permitAll()")
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

            String token = jwtUtil.generateToken(user.getEmail());

            return LoginResponse.builder()
                    .token(token)
                    .user(mapToUserResponse(user))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Login failed: Invalid credentials", e);
        }
    }

    @Override

    @PreAuthorize("permitAll()") // Anyone can access this endpoint, but we'll validate role

    public LoginResponse adminLogin(LoginRequest request) {

        try {

            authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            User user = userRepository.findByEmail(request.getEmail())

                    .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

            if (user.getRole() != Role.ADMIN) {

                throw new AccessDeniedException("Only ADMIN users are allowed to login here.");

            }

            String token = jwtUtil.generateToken(user.getEmail());

            return LoginResponse.builder()

                    .token(token)

                    .user(mapToUserResponse(user))

                    .build();

        } catch (AccessDeniedException ade) {

            throw ade;

        } catch (Exception e) {

            throw new RuntimeException("Admin login failed", e);

        }

    }



    private User mapToUserEntity(UserRequest request) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .role(request.getRole())
                .profileImageUrl(request.getProfileImageUrl())
                .dietaryPreferences(request.getDietaryPreferences())
                .allergenPreferences(request.getAllergenPreferences())
                .build();
    }

    private void updateUserData(User user, UserRequest request) {
        user.setFullName(request.getFullName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setRole(request.getRole());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setProfileImageUrl(request.getProfileImageUrl());
        user.setIsActive(request.getIsActive());
        user.setDietaryPreferences(request.getDietaryPreferences());
        user.setAllergenPreferences(request.getAllergenPreferences());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
    }

    private List<Address> convertToAddressEntities(List<AddressRequest> requests, User user) {
        if (requests == null) return Collections.emptyList();
        return requests.stream().map(req -> Address.builder()
                        .id(req.getId())
                        .addressLine1(req.getAddressLine1())
                        .addressLine2(req.getAddressLine2())
                        .city(req.getCity())
                        .state(req.getState())
                        .pinCode(req.getPinCode())
                        .latitude(req.getLatitude())
                        .longitude(req.getLongitude())
                        .isDefault(req.getIsDefault())
                        .addressType(req.getAddressType())
                        .user(user)
                        .build())
                .collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(User user) {
        List<AddressResponse> addressResponses = Optional.ofNullable(user.getAddresses())
                .orElse(Collections.emptyList())
                .stream()
                .map(addr -> AddressResponse.builder()
                        .id(addr.getId())
                        .addressLine1(addr.getAddressLine1())
                        .addressLine2(addr.getAddressLine2())
                        .city(addr.getCity())
                        .state(addr.getState())
                        .pinCode(addr.getPinCode())
                        .latitude(addr.getLatitude())
                        .longitude(addr.getLongitude())
                        .isDefault(addr.getIsDefault())
                        .addressType(addr.getAddressType())
                        .build())
                .collect(Collectors.toList());

        List<OrderResponse> orders = new ArrayList<>();
        List<GiftCardResponse> giftCards = new ArrayList<>();
        List<PaymentTransactionResponse> transactions = new ArrayList<>();
        List<UserSubscriptionResponse> subscriptions = new ArrayList<>();
        List<ReviewResponse> reviews = new ArrayList<>();
        LoyaltyPointResponse loyaltyPoint = null;

        try {
            orders = orderClient.getOrdersByUserId(user.getId());
            giftCards = giftCardClient.getGiftCardsByUserId(user.getId());
            transactions = paymentClient.getTransactionsByUserId(user.getId());
            subscriptions = subscriptionClient.getSubscriptionsByUserId(user.getId());
            reviews = reviewClient.getReviewsByUserId(user.getId());
            loyaltyPoint = loyaltyPointClient.getByUserId(user.getId());
        } catch (Exception e) {
            log.warn("External service error for userId {}: {}", user.getId(), e.getMessage());
        }

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .role(user.getRole())
                .dateJoined(user.getDateJoined())
                .lastLogin(user.getLastLogin())
                .isActive(user.getIsActive())
                .profileImageUrl(user.getProfileImageUrl())
                .dietaryPreferences(user.getDietaryPreferences())
                .allergenPreferences(user.getAllergenPreferences())
                .addresses(addressResponses)
                .orders(orders)
                .giftCards(giftCards)
                .transactions(transactions)
                .subscriptions(subscriptions)
                .reviews(reviews)
                .loyaltyPoint(loyaltyPoint)
                .build();
    }
}