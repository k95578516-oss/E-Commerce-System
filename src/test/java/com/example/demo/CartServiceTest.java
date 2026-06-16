package com.example.demo;

import com.example.demo.Exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldReturnCartWhenCartExists() {

        User user = new User();
        user.setId(1);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);

        when(cartRepository.findByUserId(1))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartId(1))
                .thenReturn(Collections.emptyList());

        CartDTO result = cartService.getCartByUserId(1);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals(1, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenCartNotFound() {

        when(cartRepository.findByUserId(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.getCartByUserId(1)
        );
    }

    @Test
    void shouldAddItemToCart() {

        User user = new User();
        user.setId(1);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);

        Product product = new Product();
        product.setId(1);
        product.setName("Laptop");
        product.setPrice(50000);

        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(1);
        dto.setQuantity(2);

        when(cartRepository.findByUserId(1))
                .thenReturn(Optional.of(cart));

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        when(cartItemRepository.findByCartIdAndProductId(1, 1))
                .thenReturn(Optional.empty());

        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(cartItemRepository.findByCartId(1))
                .thenReturn(Collections.emptyList());

        CartDTO result =
                cartService.addItemToCart(1, dto);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        User user = new User();
        user.setId(1);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);

        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(100);
        dto.setQuantity(1);

        when(cartRepository.findByUserId(1))
                .thenReturn(Optional.of(cart));

        when(productRepository.findById(100))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.addItemToCart(1, dto)
        );
    }
}