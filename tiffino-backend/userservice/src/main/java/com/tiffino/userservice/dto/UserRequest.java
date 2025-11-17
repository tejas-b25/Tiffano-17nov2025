package com.tiffino.userservice.dto;

import com.tiffino.userservice.enums.AllergenPreferences;
import com.tiffino.userservice.enums.DietaryPreferences;
import com.tiffino.userservice.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserRequest {

    private String fullName;
    private String password;
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
    private List<AddressRequest> addresses;
}
