package com.ay.notification.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String subject, String text, String email) {
        System.out.println("Отправка письма на " + email + " | Тема: " + subject + " | Текст: " + text);
    }
}
