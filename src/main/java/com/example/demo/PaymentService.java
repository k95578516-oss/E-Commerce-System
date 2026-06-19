package com.example.demo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @CircuitBreaker(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public boolean processPayment(Order order) {

        System.out.println(
                "Processing payment for Order ID: "
                        + order.getId()
        );

        // Simulate payment success
        return true;

        /*
        For testing circuit breaker:

        throw new RuntimeException(
                "Payment Gateway Down"
        );
        */
    }

    public boolean paymentFallback(
            Order order,
            Exception ex) {

        System.out.println(
                "Circuit Breaker Activated!"
        );

        System.out.println(
                "Payment service unavailable for Order ID: "
                        + order.getId()
        );

        return false;
    }

    public void refundPayment(Order order) {

        System.out.println(
                "Refund initiated for Order ID: "
                        + order.getId()
        );
    }
}
