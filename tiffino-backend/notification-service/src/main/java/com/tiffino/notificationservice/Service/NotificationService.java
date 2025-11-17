package com.tiffino.notificationservice.Service;

import com.tiffino.notificationservice.DTO.NotificationRequest;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
ResponseEntity<?> notifyUser(NotificationRequest notificationRequest);

ResponseEntity<?> updateEmailStatus(NotificationRequest requestUpdate);
}

