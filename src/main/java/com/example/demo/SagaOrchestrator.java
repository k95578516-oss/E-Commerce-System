package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SagaOrchestrator {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;

    public SagaOrchestrator(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            PaymentService paymentService) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentService = paymentService;
    }

    @Transactional
    public OrderDTO placeOrderWithSaga(Order order) {

        order.setStatus(OrderStatus.PROCESSING);

        Order savedOrder = orderRepository.save(order);

        try {

            // STEP 1: PAYMENT
            boolean paymentSuccess =
                    paymentService.processPayment(savedOrder);

            if (!paymentSuccess) {

                savedOrder.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(savedOrder);

                return convertToDTO(savedOrder);
            }

            // STEP 2: INVENTORY
            boolean inventorySuccess =
                    reduceInventory(savedOrder);

            if (!inventorySuccess) {

                paymentService.refundPayment(savedOrder);

                savedOrder.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(savedOrder);

                return convertToDTO(savedOrder);
            }

            // STEP 3: ORDER CONFIRMED
            savedOrder.setStatus(OrderStatus.PLACED);
            orderRepository.save(savedOrder);

            return convertToDTO(savedOrder);

        } catch (Exception e) {

            paymentService.refundPayment(savedOrder);

            savedOrder.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(savedOrder);

            return convertToDTO(savedOrder);
        }
    }

    private boolean reduceInventory(Order order) {

        try {

            for (OrderItem item : order.getItems()) {

                Product product =
                        productRepository.findById(
                                item.getProduct().getId()
                        ).orElse(null);

                if (product == null) {
                    return false;
                }

                if (product.getStock() < item.getQuantity()) {
                    return false;
                }

                product.setStock(
                        product.getStock() - item.getQuantity()
                );

                productRepository.save(product);
            }

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private OrderDTO convertToDTO(Order order) {

        OrderDTO dto = new OrderDTO();

        dto.setOrderId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());

        return dto;
    }
}