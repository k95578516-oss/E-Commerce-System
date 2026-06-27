package com.example.demo;

import com.example.demo.audit.AuditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private AuditService auditService;

    @Test
    void shouldSaveProduct() {

        Category category = new Category();
        category.setId(1);

        ProductDTO dto = new ProductDTO();
        dto.setName("Laptop");
        dto.setCategoryId(1);

        Product product = new Product();
        product.setId(1);
        product.setPrice(100);
        product.setStock(10);

        when(categoryRepository.findById(1))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        doNothing().when(auditService).log(anyString(), anyString());

        ProductDTO result = productService.saveProduct(dto);

        assertNotNull(result);
        assertEquals(null , result.getName());
    }

    @Test
    void shouldReturnProductWhenExists() {

        Product product = new Product();
        product.setId(1);
        product.setName("Phone");
        product.setDeleted(false);

        when(productRepository.findByIdAndDeletedFalse(1))
                .thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals("Phone", result.getName());
    }
}