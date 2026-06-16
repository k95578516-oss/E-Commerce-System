package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public CartDTO getCart(@PathVariable int userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/{userId}/items")
    public CartDTO addItem(@PathVariable int userId,
                           @RequestBody CartItemDTO dto) {
        return cartService.addItemToCart(userId, dto);
    }

    @PutMapping("/{userId}/items/{productId}")
    public CartDTO updateItem(@PathVariable int userId,
                              @PathVariable int productId,
                              @RequestParam int quantity) {
        return cartService.updateCartItem(userId, productId, quantity);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public CartDTO removeItem(@PathVariable int userId,
                              @PathVariable int productId) {
        return cartService.removeItem(userId, productId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable int userId) {
        cartService.clearCart(userId);
    }
}