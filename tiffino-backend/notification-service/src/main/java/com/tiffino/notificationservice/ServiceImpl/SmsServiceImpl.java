package com.tiffino.notificationservice.ServiceImpl;


import com.tiffino.notificationservice.DTO.SmsRequest;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl {

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    public String sendSms(SmsRequest smsRequest) {
        Message message = Message.creator(
                new PhoneNumber(smsRequest.getToPhoneNumber()),
                new PhoneNumber(fromPhoneNumber),
                smsRequest.getMessage()
        ).create();

        return message.getSid(); // Message SID returned from Twilio
    }
}


