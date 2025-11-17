package com.tiffino.notificationservice.Controller;


import com.tiffino.notificationservice.DTO.NotificationRequest;
import com.tiffino.notificationservice.DTO.SmsRequest;
import com.tiffino.notificationservice.Entity.Notification;
import com.tiffino.notificationservice.Entity.User;
import com.tiffino.notificationservice.Repo.NotificationRepository;
import com.tiffino.notificationservice.Repo.UserRepository;
import com.tiffino.notificationservice.Service.NotificationService;
import com.tiffino.notificationservice.ServiceImpl.DailySubscriptionNotifier;
import com.tiffino.notificationservice.ServiceImpl.FCMServiceImpl;
import com.tiffino.notificationservice.ServiceImpl.SmsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class Controller {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // Create a new user
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }
        userRepository.save(user);
        return ResponseEntity.ok(user.getFirstName()+" User Email created successfully.");
    }

    @PostMapping("/send")
    public ResponseEntity<?> notifyUser(@RequestBody NotificationRequest notificationRequest) {
        return notificationService.notifyUser(notificationRequest);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateEmailStatus(@RequestBody NotificationRequest request) {
        return notificationService.updateEmailStatus(request);
    }

    @GetMapping("/logs")
    public List<Notification> getAllLogs() {
        return notificationRepository.findAll();
    }

    @Autowired
    private SmsServiceImpl smsServiceImpl;
    @PostMapping("/sendSMS")
    public String sendSms(@RequestBody SmsRequest request) {
        return smsServiceImpl.sendSms(request);
    }

@Autowired
    private FCMServiceImpl fcmServiceImpl;
    @PostMapping("/push")
    public ResponseEntity<String> sendNotification(@RequestParam String title,
                                                   @RequestParam String body,
                                                   @RequestParam String token) {
        try {
            String response = fcmServiceImpl.sendNotification(title, body, token);
            return ResponseEntity.ok("Notification sent: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send: " + e.getMessage());
        }
    }

    @Autowired
    private DailySubscriptionNotifier dailySubscriptionNotifier;

    // ✅ 1. Trigger for ALL subscribed users (loop through all)
    @PostMapping("/daily")
    public ResponseEntity<?> sendToAllSubscribedUsers() {
        for (User user : userRepository.findAll()) {
            if (user.isSubscribed()) {
                dailySubscriptionNotifier.sendEmailToSingleSubscribedUser(
                        user,
                        "Your Daily Update",
                        "Hello " + user.getFirstName() + ", this is your daily update. Meal type 1, 2, 3."
                );
            }
        }
        return ResponseEntity.ok("✅ Triggered daily emails for all subscribed users.");
    }

}


