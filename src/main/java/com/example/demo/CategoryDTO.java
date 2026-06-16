package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {
    private int id;
    @NotBlank(message = "Name can't be empty")
    @Size(min = 2, max = 50, message = "NAME MUST BE IN BETWEEN 2-50 CHARACTER")
    private String name;
    private String description;
    public CategoryDTO(){}

    public CategoryDTO(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
