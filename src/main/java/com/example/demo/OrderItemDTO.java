package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


public class OrderItemDTO {

    private int productId;
    @NotBlank(message = "Product name can't be empty")
    private String productName;

   @Positive
    private int quantity;

    private double price;

    public OrderItemDTO(){}

    public OrderItemDTO(int productid,String productName, int quantity, double price){
        this.productId = productid;
        this.productName = productName;
        this.quantity =   quantity;
        this.price = price;
    }

    public int getProductId(){
        return productId;
    }
    public void setProductId(int productId){
        this.productId = productId;
    }
    public String getProductName(){
        return productName;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }
    public int getQuantity(){
        return  quantity;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }
}
