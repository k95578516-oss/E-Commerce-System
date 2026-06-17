package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderService orderService;


    @Test
    void shouldPlaceOrder() {

        User user = new User();
        user.setId(1);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);

        Product product = new Product();
        product.setId(1);
        product.setPrice(50000);
        product.setStock(10);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        when(cartRepository.findByUserId(1))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartId(1))
                .thenReturn(List.of(cartItem));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderDTO result =
                orderService.placeOrder(1);

        assertNotNull(result);
        assertEquals("PLACED", result.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCartIsEmpty() {

        User user = new User();
        user.setId(1);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);

        when(cartRepository.findByUserId(1))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartId(1))
                .thenReturn(List.of());

        assertThrows(
                RuntimeException.class,
                () -> orderService.placeOrder(1)
        );
    }
}
