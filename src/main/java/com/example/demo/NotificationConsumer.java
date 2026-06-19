package com.example.demo;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "inventory-events", groupId = "notification-group")
    public void consume(OrderEvent event) {

        System.out.println("🔔 Notification Service received: " + event.getStatus());

        String email = "user" + event.getUserId() + "@gmail.com";

        switch (event.getStatus()) {

            case "INVENTORY_SUCCESS":
                emailService.sendEmail(
                        email,
                        "Order Confirmed 🎉",
                        "Your order " + event.getOrderId() + " is confirmed!"
                );
                break;

            case "INVENTORY_FAILED":
                emailService.sendEmail(
                        email,
                        "Order Failed ❌",
                        "Your payment was successful but inventory failed. Refund will be initiated."
                );
                break;

            case "PAYMENT_FAILED":
                emailService.sendEmail(
                        email,
                        "Payment Failed ❌",
                        "Your payment failed. Please try again."
                );
                break;

            default:
                emailService.sendEmail(
                        email,
                        "Order Update",
                        "Your order status: " + event.getStatus()
                );
        }
    }
}
