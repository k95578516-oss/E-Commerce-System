package com.example.demo;

import com.example.demo.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public List<CategoryDTO> getAllCategory() {

        List<Category> category = repo.findAll();

        return category.stream().map(this::convertToDTO).toList();
    }

    public CategoryDTO getCategoryById(int id) {

        Category category = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id));

        return convertToDTO(category);
    }

    public CategoryDTO saveCategory(CategoryDTO dto) {

        Category category = convertToEntity(dto);

        Category savedCategory = repo.save(category);

        return convertToDTO(savedCategory);
    }

    public CategoryDTO updateCategory(int id, CategoryDTO dto) {

        Category category = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(dto.getName());


        Category updatedCategory = repo.save(category);

        return convertToDTO(updatedCategory);
    }

    public void deleteCategory(int id) {

        Category category = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id));

        repo.delete(category);
    }

    private CategoryDTO convertToDTO(Category Category) {

        CategoryDTO dto = new CategoryDTO();

        dto.setId(Category.getId());
        dto.setName(Category.getName());
        dto.setDescription(Category.getDescription());

        return dto;
    }

    private Category convertToEntity(CategoryDTO dto) {

        Category category = new Category();

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;

    }

}
