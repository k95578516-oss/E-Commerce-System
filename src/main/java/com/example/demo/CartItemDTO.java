package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CartItemDTO {

    private int id;
    private int productId;
    @NotBlank(message = "productName can't be empty")
    @Size(min = 2, max = 50,
            message =  "productName should in between 2-50 character")
    private String productName;
    @Positive
    private int quantity;
    @Positive
    private double price;

    public CartItemDTO(){}

    public CartItemDTO(int id, int productId, String productName, int quantity, double price){
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
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
