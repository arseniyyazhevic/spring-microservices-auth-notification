package com.ay.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${app.mail.enabled}")
    private boolean mailEnabled;
    private final JavaMailSender javaMailSender;
    public void sendEmail(String subject, String text, String email) {
        if (mailEnabled) {
            System.out.println("Send letter on " + email + " | Topic: " + subject + " | Text: " + text);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
