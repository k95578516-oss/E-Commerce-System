package com.example.demo;

import java.util.List;

public class CartDTO {

    private int id;
    private int userId;
    private List<CartItemDTO> items;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public List<CartItemDTO> getItems() { return items; }
    public void setItems(List<CartItemDTO> items) { this.items = items; }
}