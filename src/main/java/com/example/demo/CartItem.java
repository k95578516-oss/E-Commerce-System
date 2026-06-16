package com.example.demo;

import jakarta.persistence.*;


@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantity;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public int getId(){
        return id;
    }
    public int getQuantity(){
  return quantity;
    }

    public Cart getCart(){
        return cart;
    }
    public Product getProduct(){
        return product;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void setCart(Cart cart){
        this.cart = cart;
    }
    public void setProduct(Product product){
        this.product = product;
    }
}
