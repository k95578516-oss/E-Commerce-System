package com.example.demo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderEvent implements Serializable {

    private int orderId;
    private int userId;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;

    public OrderEvent() {
        this.createdAt = LocalDateTime.now();
    }

    public OrderEvent(int orderId, int userId, Double totalAmount, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = LocalDateTime.now();
    }

    // getters & setters

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}