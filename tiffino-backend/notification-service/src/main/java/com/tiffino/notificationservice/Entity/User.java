package com.tiffino.notificationservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String firstName;
    private String lastName;

    private boolean subscriptionActive;  // ✅ true if the user has an active subscription

    // ✅ Add this method at the bottom of the class:
    public boolean isSubscribed() {
        return this.subscriptionActive;
    }

}
