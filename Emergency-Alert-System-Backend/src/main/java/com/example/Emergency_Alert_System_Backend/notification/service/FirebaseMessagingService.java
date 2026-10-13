package com.example.Emergency_Alert_System_Backend.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    public String sendNotification(
            String fcmToken,
            String title,
            String body) {

        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(
                            Notification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .build()
                    )
                    .putData("type", "EMERGENCY")
                    .build();

            return FirebaseMessaging.getInstance().send(message);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to send FCM notification: " + e.getMessage(),
                    e
            );
        }
    }
}
