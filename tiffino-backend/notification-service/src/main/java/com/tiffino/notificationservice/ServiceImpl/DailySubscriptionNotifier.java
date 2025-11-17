package com.tiffino.notificationservice.ServiceImpl;


import com.tiffino.notificationservice.Entity.*;
import com.tiffino.notificationservice.Repo.NotificationRepository;
import com.tiffino.notificationservice.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailySubscriptionNotifier {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final String mailFrom = "rajeshgahilod1435@gmail.com";
    // Send every day at 10:00 AM (server time)
    @Scheduled(cron = "0 5 10 * * *") // ⏰ 10:05 AM every day
    public void sendEmailsToAllSubscribedUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.isSubscribed()) {
                sendEmailToSingleSubscribedUser(
                        user,
                        "Your Subscription Update",
                        "Hello " + user.getFirstName() + ", this is your scheduled update."
                );
            }
        }
    }

    public ResponseEntity<?> sendEmailToSingleSubscribedUser(User user, String subject, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setSubject(subject);
        notification.setMessage("Hi sub " + user.getFirstName() + ",\n\n" + message);
        notification.setStatus(Status.PENDING);
        notification.setType(NotificationType.EMAIL);
        notification.setEmail(user.getEmail());
        try {
            if (user.isSubscribed()) {
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setTo(user.getEmail());
                mail.setSubject(subject);
                mail.setText(notification.getMessage());
                mail.setFrom(mailFrom);
                mailSender.send(mail);
                notification.setStatus(Status.SENT);
            } else {
                notification.setStatus(Status.SKIPPED);
                notification.setErrorMessage("User is not subscribed.");
            }
        } catch (MailException e) {
            notification.setStatus(Status.FAILED);
            notification.setErrorMessage(e.getMessage());
        }
        notificationRepository.save(notification);
        if (notification.getStatus() == Status.SENT) {
            return new  ResponseEntity<>("Subscription email sent to " + user.getEmail(),HttpStatus.OK);
        } else if (notification.getStatus() == Status.SKIPPED) {
            return new ResponseEntity<>("Skipped: User not subscribed.",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to send subscription email: " + notification.getErrorMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


}

