package com.tiffino.userservice.dto;


import com.tiffino.userservice.entity.Address;
import com.tiffino.userservice.enums.AllergenPreferences;
import com.tiffino.userservice.enums.DietaryPreferences;
import com.tiffino.userservice.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Builder
@Data
public class UserDto {

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
        private Boolean isActive = true;
        private String profileImageUrl;
        private DietaryPreferences dietaryPreferences;
        private AllergenPreferences allergenPreferences;
        private List<Address> addresses = new ArrayList<>();


}
