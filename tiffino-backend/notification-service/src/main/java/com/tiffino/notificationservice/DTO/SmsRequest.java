package com.tiffino.notificationservice.DTO;


import lombok.Data;

@Data
public class SmsRequest {
    private String toPhoneNumber;
    private String message;
}
