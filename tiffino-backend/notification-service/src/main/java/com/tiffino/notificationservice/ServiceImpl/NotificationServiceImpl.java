package com.tiffino.notificationservice.ServiceImpl;

import com.tiffino.notificationservice.DTO.NotificationRequest;
import com.tiffino.notificationservice.Entity.*;
import com.tiffino.notificationservice.Repo.NotificationRepository;
import com.tiffino.notificationservice.Repo.UserRepository;
import com.tiffino.notificationservice.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.tiffino.notificationservice.Entity.NotificationStatusType.*;


@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    SmsServiceImpl smsServiceImpl;

    @Autowired
    DailySubscriptionNotifier dailySubscriptionNotifier;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    public ResponseEntity<?> notifyUser(NotificationRequest notificationRequest) {
        String email = notificationRequest.getEmail();
        String subject = notificationRequest.getSubject();
        String message = notificationRequest.getMessage();
        NotificationType type = notificationRequest.getType();

        // 1. Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found."));

        // 2. Create notification entity
        Notification notification = new Notification();
        notification.setEmail(email);
        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setType(type);
//        notification.setStatusType(statusType);
        notification.setUser(user);

        // 3. Route based on type
        switch (type) {
            case EMAIL:
                return sendEmailNotification(user, notification);

            case EMAIL_SUBSCRIPTION:
                return dailySubscriptionNotifier.sendEmailToSingleSubscribedUser(user, subject, message);

            case SMS:
                notification.setStatus(Status.FAILED);
                notification.setErrorMessage("SMS notifications not yet implemented.");
                notificationRepository.save(notification);
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                        .body(notification.getErrorMessage());

            case PUSH:
                notification.setStatus(Status.FAILED);
                notification.setErrorMessage("Push notifications not yet implemented.");
                notificationRepository.save(notification);
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                        .body(notification.getErrorMessage());

            default:
                return ResponseEntity.badRequest().body("Unsupported notification type.");
        }
    }

    private ResponseEntity<?> sendEmailNotification(User user, Notification notification) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(notification.getSubject());
            mailMessage.setText("Hi " + user.getFirstName() + ",\n\n" + notification.getMessage());
            mailMessage.setFrom(mailFrom);
            mailSender.send(mailMessage);
            notification.setStatus(Status.PENDING);
            notification.setErrorMessage(null);
        } catch (MailException e) {
            //  Set failure status
            notification.setStatus(Status.FAILED);
            notification.setErrorMessage(e.getMessage());
        }
        notificationRepository.save(notification);
        // Return based on status
        if (notification.getStatus() == Status.PENDING) {
            return ResponseEntity.ok(" Email sent to " + user.getEmail());
        } else {
            notification.setStatus(Status.FAILED);
            return new ResponseEntity<>("Failed to send email: " + notification.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Async
    public void sendAsyncEmail(User user, Notification notification) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(notification.getSubject());
            mailMessage.setText("Hello " + user.getFirstName() + ",\n\n" + notification.getMessage());
            mailMessage.setFrom(mailFrom);
            mailSender.send(mailMessage);

            notification.setStatus(Status.UPDATE);
            notification.setErrorMessage(null);
        } catch (Exception e) {
            notification.setStatus(Status.FAILED);
            notification.setErrorMessage("Failed to send email: " + e.getMessage());
        } finally {
            notificationRepository.save(notification);
        }
    }

    @Override
    public ResponseEntity<?> updateEmailStatus(NotificationRequest requestUpdate) {
        String email = requestUpdate.getEmail();
        NotificationStatusType statusType = requestUpdate.getStatusType();

        // ✅ 1. Check if user exists
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ User with email " + email + " not found. Notification not sent.");
        }

        User user = optionalUser.get();

        // ✅ 2. Check for last notification (used for cancel rule)
        Notification lastNotification = notificationRepository
                .findTopByUserEmailOrderByIdDesc(email)
                .orElse(null);

        // ✅ 3. Allow ORDER_CANCELLED only if last was ORDER_ON_THE_WAY
        if (statusType == NotificationStatusType.ORDER_CANCELLED) {
            if (lastNotification == null || lastNotification.getStatusType() != NotificationStatusType.ORDER_ON_THE_WAY) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(" Cannot cancel. Order was not confirmed (ORDER_ON_THE_WAY) before.");
            }
        }

        Notification notification = notificationRepository
                .findTopByUserEmailOrderByIdDesc(email)
                .orElse(new Notification());

        notification.setUser(user);
        notification.setEmail(user.getEmail());

        switch (statusType) {
            case ORDER_CANCELLED:
                notification.setSubject("Order Cancelled");
                notification.setMessage("Your order has been cancelled successfully.");
                notification.setStatusType(ORDER_CANCELLED);
                break;
            case ORDER_DELIVERED:
                notification.setSubject("Order Delivered");
                notification.setMessage("Your order has been delivered. Thank you for shopping!");
                notification.setStatusType(ORDER_DELIVERED);
                break;
            case ORDER_ON_THE_WAY:// tracer
                notification.setStatusType(ORDER_ON_THE_WAY);

            default:
                notification.setSubject("Order On the Way");
                notification.setMessage("You can track your order.");
                break;
        }

        notification.setStatus(Status.PENDING);
        notification.setType(NotificationType.EMAIL);

        notification.setErrorMessage(null);

        notificationRepository.save(notification);

        sendAsyncEmail(user, notification);

        return ResponseEntity.ok("✅ Email notification triggered for: " + statusType + " to " + user.getEmail());
    }



}
