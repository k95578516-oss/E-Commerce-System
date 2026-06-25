package com.example.demo;

import com.example.demo.Exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    // ================= GET ALL =================
    public List<CategoryDTO> getAllCategory() {
        return repo.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ================= GET BY ID =================
    public CategoryDTO getCategoryById(int id) {
        Category category = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found: " + id));

        return toDTO(category);
    }

    // ================= CREATE =================
    public CategoryDTO saveCategory(CategoryDTO dto) {

        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        Category saved = repo.save(category);

        return toDTO(saved);
    }

    // ================= UPDATE =================
    public CategoryDTO updateCategory(int id, CategoryDTO dto) {

        Category category = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found: " + id));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        Category updated = repo.save(category);

        return toDTO(updated);
    }

    // ================= DELETE =================
    public void deleteCategory(int id) {

        Category category = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found: " + id));

        repo.delete(category);
    }

    // ================= MAPPER =================
    private CategoryDTO toDTO(Category category) {
        return new CategoryDTO(
                category.getName(),
                category.getDescription()
        );
    }
}
