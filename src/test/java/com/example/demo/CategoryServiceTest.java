package com.example.demo;
import com.example.demo.Exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

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

        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(1);
        dto.setQuantity(2);

        when(cartRepository.findByUserId(1))
                .thenReturn(Optional.of(cart));

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        when(cartItemRepository.findByCartIdAndProductId(1,1))
                .thenReturn(Optional.empty());

        CartDTO result =
                cartService.addItemToCart(1,dto);

        assertNotNull(result);
    }
    @Test
    void shouldThrowExceptionWhenCartNotFound() {

        CartItemDTO dto = new CartItemDTO();

        when(cartRepository.findByUserId(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.addItemToCart(1,dto)
        );
    }
}
