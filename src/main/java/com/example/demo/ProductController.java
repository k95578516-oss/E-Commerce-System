package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductDTO create( @Valid @RequestBody ProductDTO dto) {
        return productService.saveProduct(dto);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable int id, @RequestBody ProductDTO dto) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDTO> getByCategory(@PathVariable int categoryId) {
        return productService.getProductByCategory(categoryId);
    }

    @GetMapping("/page")
    public Object getPaged(@RequestParam int page,
                           @RequestParam int size,
                           @RequestParam String sortBy) {
        return productService.getProduct(page, size, sortBy);
    }
    @GetMapping("/search")
    public List<ProductDTO> searchProducts(
            @RequestParam String keyword) {

        return productService.searchProducts(keyword);
    }
}