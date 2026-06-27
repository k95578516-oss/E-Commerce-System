package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock private CategoryRepository repo;

    @Test
    void shouldSaveCategory() {

        CategoryDTO dto = new CategoryDTO();
        dto.setName("Electronics");

        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        when(repo.save(any(Category.class)))
                .thenReturn(category);

        CategoryDTO result = categoryService.saveCategory(dto);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
    }

    @Test
    void shouldGetCategoryById() {

        Category category = new Category();
        category.setId(1);
        category.setName("Test");

        when(repo.findById(1))
                .thenReturn(Optional.of(category));

        CategoryDTO result = categoryService.getCategoryById(1);

        assertNotNull(result);
        assertEquals("Test", result.getName());
    }
}