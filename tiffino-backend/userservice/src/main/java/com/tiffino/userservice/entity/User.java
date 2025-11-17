package com.tiffino.userservice.entity;

import com.tiffino.userservice.enums.AllergenPreferences;
import com.tiffino.userservice.enums.DietaryPreferences;
import com.tiffino.userservice.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String gender;

    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime dateJoined;
    private LocalDateTime lastLogin;
    private Boolean isActive = true;
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private DietaryPreferences dietaryPreferences;

    @Enumerated(EnumType.STRING)
    private AllergenPreferences allergenPreferences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();


}
