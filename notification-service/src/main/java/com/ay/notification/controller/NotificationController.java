package com.ay.notification.controller;

import com.ay.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody Map<String, String> body) {
        String subject = body.get("subject");
        String text = body.get("text");
        String email = body.get("email");
        emailService.sendEmail(subject, text, email);
        return ResponseEntity.ok().build();
    }
}
