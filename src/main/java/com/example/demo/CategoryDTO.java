package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {

    @NotBlank(message = "Name can't be empty")
    @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
    private String name;

    private String description;

    public CategoryDTO() {}

    public CategoryDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}