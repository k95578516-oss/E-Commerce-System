package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductDTO create(@Valid @RequestBody ProductDTO dto) {
        return productService.saveProduct(dto);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable int id,
                             @RequestBody ProductDTO dto) {
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

    // ✅ FIXED PAGINATION (IMPORTANT)
    @GetMapping("/page")
    public Page<ProductDTO> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return productService.getProduct(page, size, sortBy);
    }

    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }
}