package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SagaOrchestrator {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentService paymentService;

    // ================= START SAGA =================
    @Transactional
    public OrderDTO placeOrderWithSaga(Order order) {

        // STEP 1: CREATE ORDER (PENDING STATE)
        order.setStatus("PENDING");
        Order savedOrder = orderRepository.save(order);

        try {

            // STEP 2: PAYMENT
            boolean paymentSuccess = paymentService.processPayment(savedOrder);

            if (!paymentSuccess) {
                savedOrder.setStatus("PAYMENT_FAILED");
                orderRepository.save(savedOrder);
                return convertToDTO(savedOrder);
            }

            savedOrder.setStatus("PAYMENT_SUCCESS");
            orderRepository.save(savedOrder);

            // STEP 3: INVENTORY REDUCTION
            boolean inventorySuccess = reduceInventory(savedOrder);

            if (!inventorySuccess) {

                // COMPENSATION → REFUND PAYMENT
                paymentService.refundPayment(savedOrder);

                savedOrder.setStatus("CANCELLED_INVENTORY_FAILED");
                orderRepository.save(savedOrder);

                return convertToDTO(savedOrder);
            }

            // STEP 4: SUCCESS
            savedOrder.setStatus("COMPLETED");
            orderRepository.save(savedOrder);

            return convertToDTO(savedOrder);

        } catch (Exception e) {

            // GLOBAL FAILURE HANDLING

            paymentService.refundPayment(savedOrder);

            savedOrder.setStatus("FAILED");
            orderRepository.save(savedOrder);

            return convertToDTO(savedOrder);
        }
    }

    // ================= INVENTORY LOGIC =================
    private boolean reduceInventory(Order order) {

        try {

            for (OrderItem item : order.getItems()) {

                Product product = item.getProduct();

                if (product.getStock() < item.getQuantity()) {
                    return false;
                }

                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // ================= DTO MAPPER =================
    private OrderDTO convertToDTO(Order order) {

        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());

        return dto;
    }
}