package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/{id}")
    public CategoryDTO getById(@PathVariable int id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public CategoryDTO create(@Valid @RequestBody CategoryDTO dto) {
        return categoryService.saveCategory(dto);
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable int id,
                              @Valid @RequestBody CategoryDTO dto) {
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        categoryService.deleteCategory(id);
    }
}