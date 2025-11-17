package com.tiffino.notificationservice.DTO;

import com.tiffino.notificationservice.Entity.*;
import lombok.Data;

@Data
public class NotificationRequest {
    private String email;
    private String subject;
    private String message;
    private NotificationType type;
    private Status status;
    private String errorMessage;
    private User user;
    private NotificationStatusType StatusType;


}

