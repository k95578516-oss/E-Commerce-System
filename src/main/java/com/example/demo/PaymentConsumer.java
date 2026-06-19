package com.example.demo;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentConsumer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    public void consume(OrderEvent event) {

        System.out.println("💳 Payment processing for order: " + event.getOrderId());

        boolean paymentSuccess = true; // simulate

        if (paymentSuccess) {

            kafkaTemplate.send("payment-events",
                    new OrderEvent(event.getOrderId(), event.getUserId(),
                            event.getTotalAmount(), "PAYMENT_SUCCESS"));

        } else {

            kafkaTemplate.send("payment-events",
                    new OrderEvent(event.getOrderId(), event.getUserId(),
                            event.getTotalAmount(), "PAYMENT_FAILED"));
        }
    }
}