package com.example.demo;

import com.example.demo.audit.AuditService;
import com.example.demo.outbox.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock private OrderRepository orderRepository;
    @Mock private CartRepository cartRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ProductRepository productRepository;
    @Mock private OutboxRepository outboxRepository;
    @Mock private ObjectMapper objectMapper;
    @Mock private AuditService auditService;

    @Test
    void shouldPlaceOrderSuccessfully() throws Exception {

        User user = new User();
        user.setId(1);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);

        Product product = new Product();
        product.setId(1);
        product.setPrice(100);
        product.setStock(10); // 🔥 CRITICAL FIX

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);

        when(cartRepository.findByUserId(1))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartId(1))
                .thenReturn(List.of(item));

        when(productRepository.save(any()))
                .thenReturn(product);

        when(orderRepository.saveAndFlush(any()))
                .thenAnswer(i -> {
                    Order o = i.getArgument(0);
                    o.setId(1);
                    o.setUser(user);
                    o.setStatus(OrderStatus.PLACED);
                    return o;
                });

        when(objectMapper.writeValueAsString(any()))
                .thenReturn("{}");

        OrderDTO dto = orderService.placeOrder(1);

        assertNotNull(dto);
        assertEquals("PLACED", dto.getStatus());
    }
}