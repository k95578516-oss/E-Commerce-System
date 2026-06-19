package com.example.demo;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumer {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryConsumer(ProductRepository productRepository,
                             KafkaTemplate<String, Object> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-events", groupId = "inventory-group")
    public void consume(OrderEvent event) {

        System.out.println("📦 Inventory Service received order: " + event.getOrderId());

        try {
            boolean stockOk = checkAndReduceStock(event);

            if (stockOk) {
                kafkaTemplate.send("inventory-events",
                        new InventoryEvent(event.getOrderId(), event.getUserId(), "INVENTORY_SUCCESS"));

                System.out.println("✅ Inventory updated successfully");
            } else {
                kafkaTemplate.send("inventory-events",
                        new InventoryEvent(event.getOrderId(), event.getUserId(), "INVENTORY_FAILED"));

                System.out.println("❌ Inventory failed");
            }

        } catch (Exception e) {
            System.out.println("🔥 Inventory error: " + e.getMessage());
        }
    }

    private boolean checkAndReduceStock(OrderEvent event) {

        // 🔥 REAL LOGIC (simplified for learning)
        // In production: you would fetch order items from DB or event payload

        Product product = productRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() <= 0) {
            return false;
        }

        product.setStock(product.getStock() - 1);
        productRepository.save(product);

        return true;
    }
}