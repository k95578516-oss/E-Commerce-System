package com.example.demo;

import java.time.LocalDateTime;

public class InventoryEvent {

    private int orderId;
    private int userId;
    private String status; // INVENTORY_SUCCESS / INVENTORY_FAILED
    private LocalDateTime createdAt;

    public InventoryEvent() {
        this.createdAt = LocalDateTime.now();
    }

    public InventoryEvent(int orderId, int userId, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}