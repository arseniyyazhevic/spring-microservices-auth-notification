package com.ay.auth.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String notificationUrl = "http://notification-service:8090/api/notifications";

    public void sendNotification(String subject, String text, String email) {
        Map<String, String> body = Map.of(
                "subject", subject,
                "text", text,
                "email", email
        );
        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(notificationUrl, body, Void.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Ошибка отправки уведомления");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при вызове NotificationService: " + e.getMessage());
        }
    }
}

