package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantity;

    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    public int getId(){
        return id;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public Product getProduct(){
        return product;
    }
    public void setProduct(Product product){
        this.product = product;
    }
    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public Order getOrder(){
        return order;
    }
    public void setOrder(Order order){
        this.order = order;
    }
}
