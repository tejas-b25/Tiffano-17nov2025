package com.tiffino.notificationservice.Repo;

import com.tiffino.notificationservice.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // ✅ This is the method
    Optional<Notification> findTopByUserEmailOrderByIdDesc(String email);
}