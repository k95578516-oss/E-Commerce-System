package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String to, String subject, String body) {

        // 🔥 In real system: SMTP / SES / SendGrid
        System.out.println("📧 EMAIL SENT");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }
}
