package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock private CartRepository cartRepository;
    @Mock private UserRepository userRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ProductRepository productRepository;

    @Test
    void addItem_shouldCreateCartAndReturnDTO() {

        int userId = 1;

        User user = new User();
        user.setId(userId);

        Cart cart = new Cart();
        cart.setId(10);
        cart.setUser(user);

        Product product = new Product();
        product.setId(5);
        product.setName("Phone");
        product.setPrice(1000.0);
        product.setStock(10);

        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(5);
        dto.setQuantity(2);

        when(cartRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(cartRepository.save(any()))
                .thenReturn(cart);

        when(productRepository.findById(5))
                .thenReturn(Optional.of(product));

        when(cartItemRepository.findByCartIdAndProductId(anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        when(cartItemRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(cartItemRepository.findByCartId(anyInt()))
                .thenReturn(List.of());

        when(cartRepository.findById(anyInt()))
                .thenReturn(Optional.of(cart));

        CartDTO result = cartService.addItemToCart(userId, dto);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }
}