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
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String subject;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationStatusType StatusType;

    @Enumerated(EnumType.STRING) // ✅ store enum as string
    @Column(length = 20)         // ✅ optional, but makes space in DB
    private Status status;

    private String errorMessage; // Optional: for debugging failures

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
