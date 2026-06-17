package com.example.demo;

import com.example.demo.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderDTO placeOrder(int userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Create Order
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        order.setStatus("PLACED");

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if(product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        product.getName() + " is out of stock"
                );
            }

            product.setStock(
                    product.getStock()
                            - cartItem.getQuantity()
            );

            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);

            totalAmount +=
                    product.getPrice()
                            * cartItem.getQuantity();

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // clear cart after order
        cartItemRepository.deleteAll(cartItems);

        return convertToDTO(savedOrder);
    }

    // ================= GET ORDER BY ID =================
    public OrderDTO getOrderById(int orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        return convertToDTO(order);
    }

    // ================= GET ALL ORDERS OF USER =================
    public List<OrderDTO> getOrdersByUserId(int userId) {

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ================= CANCEL ORDER =================
    public OrderDTO cancelOrder(int orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if ("SHIPPED".equals(order.getStatus())) {
            throw new RuntimeException("Cannot cancel shipped order");
        }

        order.setStatus("CANCELLED");

        Order updated = orderRepository.save(order);

        return convertToDTO(updated);
    }

    // ================= MAPPER =================
    private OrderDTO convertToDTO(Order order) {

        List<OrderItemDTO> items = orderItemRepository
                .findByOrderId(order.getId())
                .stream()
                .map(this::convertItemToDTO)
                .toList();

        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setItems(items);

        return dto;
    }

    private OrderItemDTO convertItemToDTO(OrderItem item) {

        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());

        return dto;
    }
}