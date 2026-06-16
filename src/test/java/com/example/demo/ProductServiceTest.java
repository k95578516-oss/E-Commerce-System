package com.example.demo;

import com.example.demo.Exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    void shouldReturnProductWhenProductExists() {

        Category category = new Category();
        category.setId(1);

        Product product = new Product();
        product.setId(1);
        product.setName("Laptop");
        product.setPrice(50000);
        product.setCategory(category);

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals(50000, result.getPrice());
    }
    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        when(productRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductById(1)
        );
    }

    @Test
    void shouldSaveProduct() {

        Category category = new Category();
        category.setId(1);

        Product product = new Product();
        product.setId(1);
        product.setName("Laptop");
        product.setCategory(category);

        ProductDTO dto = new ProductDTO();
        dto.setName("Laptop");
        dto.setCategoryId(1);

        when(categoryRepository.findById(1))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        ProductDTO result = productService.saveProduct(dto);

        assertEquals("Laptop", result.getName());
    }
}
