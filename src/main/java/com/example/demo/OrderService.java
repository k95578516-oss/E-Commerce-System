package com.example.demo;

import com.example.demo.audit.AuditService;
import com.example.demo.outbox.OutboxEvent;
import com.example.demo.outbox.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AuditService auditService;

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        ProductRepository productRepository,
                        OutboxRepository outboxRepository,
                        ObjectMapper objectMapper, AuditService auditService) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
        this.auditService = auditService;
    }

    // ================= MAIN ORDER FLOW =================
    @Transactional
    public OrderDTO placeOrder(int userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PLACED);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException(product.getName() + " out of stock");
            }

            try {

                product.setStock(
                        product.getStock() - item.getQuantity()
                );

                productRepository.save(product);

            } catch (Exception e) {

                throw new RuntimeException(
                        "Concurrent stock update detected. Retry order."
                );
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);

            totalAmount += product.getPrice() * item.getQuantity();
        }
        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }
        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.saveAndFlush(order);


        // clear cart
        cartItemRepository.deleteAll(cartItems);

        // ================= OUTBOX EVENT (IMPORTANT FIX) =================
        try {

            OrderEvent event = new OrderEvent(
                    savedOrder.getId(),
                    savedOrder.getUser().getId(),

                    savedOrder.getTotalAmount(),
                    "ORDER_CREATED"
            );

            OutboxEvent outbox = new OutboxEvent();
            outbox.setEventType("ORDER_CREATED");
            outbox.setPayload(objectMapper.writeValueAsString(event));

            outboxRepository.save(outbox);
            auditService.log(
                    "USER_" + userId,
                    "ORDER_PLACED : " + savedOrder.getId()
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to create outbox event", e);
        }

        return convertToDTO(savedOrder);
    }

    // ================= GET ORDER =================
    public OrderDTO getOrderById(int orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return convertToDTO(order);
    }

    // ================= USER ORDERS =================
    public List<OrderDTO> getOrdersByUserId(int userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ================= CANCEL ORDER =================
    @Transactional
    public OrderDTO cancelOrder(int orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.SHIPPED ||
                order.getStatus() == OrderStatus.DELIVERED) {

            throw new RuntimeException(
                    "Cannot cancel shipped or delivered order");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {

            throw new RuntimeException(
                    "Order already cancelled");
        }

        for (OrderItem item : order.getItems()) {

            Product product = item.getProduct();

            product.setStock(
                    product.getStock()
                            + item.getQuantity()
            );

            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order savedOrder = orderRepository.save(order);

        auditService.log(
                "ADMIN",
                "ORDER_CANCELLED : "
                        + orderId
        );

        return convertToDTO(savedOrder);
    }
    @Transactional
    public OrderDTO updateStatus(
            Integer orderId,
            OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        OrderStatus currentStatus = order.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new RuntimeException(
                    "Invalid status transition from "
                            + currentStatus
                            + " to "
                            + newStatus);
        }

        order.setStatus(newStatus);

        Order savedOrder = orderRepository.save(order);

        auditService.log(
                "ADMIN",
                "ORDER_STATUS_UPDATED : "
                        + orderId
                        + " -> "
                        + newStatus
        );

        return convertToDTO(savedOrder);
    }

    // ================= DTO =================
    private OrderDTO convertToDTO(Order order) {

        List<OrderItemDTO> items = order.getItems()
                .stream()
                .map(this::convertItemToDTO)
                .toList();

        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
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
    private boolean isValidTransition(
            OrderStatus current,
            OrderStatus next) {

        switch(current) {

            case PLACED:
                return next == OrderStatus.PROCESSING
                        || next == OrderStatus.CANCELLED;

            case PROCESSING:
                return next == OrderStatus.SHIPPED
                        || next == OrderStatus.CANCELLED;

            case SHIPPED:
                return next == OrderStatus.DELIVERED;

            case DELIVERED:
                return false;

            case CANCELLED:
                return false;

            default:
                return false;
        }
    }
}