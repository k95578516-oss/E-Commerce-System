package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // PLACE ORDER (CHECKOUT)
    @PostMapping("/{userId}")
    public OrderDTO placeOrder(@PathVariable int userId) {
        return orderService.placeOrder(userId);
    }

    // GET ORDER BY ID
    @GetMapping("/{orderId}")
    public OrderDTO getOrder(@PathVariable int orderId) {
        return orderService.getOrderById(orderId);
    }

    // GET USER ORDERS
    @GetMapping("/user/{userId}")
    public List<OrderDTO> getUserOrders(@PathVariable int userId) {
        return orderService.getOrdersByUserId(userId);
    }

    // CANCEL ORDER
    @PutMapping("/{orderId}/cancel")
    public OrderDTO cancelOrder(@PathVariable int orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PutMapping("/{orderId}/status")
    public OrderDTO updateStatus(
            @PathVariable Integer orderId,
            @RequestParam OrderStatus status) {

        return orderService.updateStatus(orderId, status);
    }
}
