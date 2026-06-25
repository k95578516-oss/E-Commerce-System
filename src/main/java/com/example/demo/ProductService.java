package com.example.demo;

import com.example.demo.audit.AuditService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AuditService auditService;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          AuditService auditService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.auditService = auditService;
    }

    // ================= GET ALL =================
    @Cacheable("allProducts")
    public List<ProductDTO> getAllProducts() {
        return productRepository.findByDeletedFalse()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ================= GET BY ID =================
    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProductById(int id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return convertToDTO(product);
    }

    // ================= CREATE =================
    @CacheEvict(value = {"products", "allProducts", "productsByCategory"}, allEntries = true)
    public ProductDTO saveProduct(ProductDTO dto) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(category);

        Product saved = productRepository.save(product);

        auditService.log("ADMIN", "PRODUCT_CREATED : " + saved.getId());

        return convertToDTO(saved);
    }

    // ================= UPDATE =================
    @CacheEvict(value = {"products", "allProducts", "productsByCategory"}, allEntries = true)
    public ProductDTO updateProduct(int id, ProductDTO dto) {

        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        if (dto.getCategoryId() > 0) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        Product updated = productRepository.save(product);

        auditService.log("ADMIN", "PRODUCT_UPDATED : " + updated.getId());

        return convertToDTO(updated);
    }

    // ================= DELETE (SOFT DELETE) =================
    @CacheEvict(value = {"products", "allProducts", "productsByCategory"}, allEntries = true)
    public void deleteProduct(int id) {

        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setDeleted(true);
        productRepository.save(product);

        auditService.log("ADMIN", "PRODUCT_DELETED : " + product.getId());
    }

    // ================= CATEGORY =================
    @Cacheable(value = "productsByCategory", key = "#categoryId")
    public List<ProductDTO> getProductByCategory(int categoryId) {

        return productRepository.findByCategoryIdAndDeletedFalse(categoryId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ================= SEARCH =================
    public List<ProductDTO> searchProducts(String keyword) {

        return productRepository
                .findByNameContainingIgnoreCaseAndDeletedFalse(keyword)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ================= PAGINATION FIX =================
    public Page<ProductDTO> getProduct(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy)
        );

        Page<Product> productPage = productRepository.findByDeletedFalse(pageable);

        return productPage.map(this::convertToDTO);
    }

    // ================= MAPPER =================
    private ProductDTO convertToDTO(Product product) {

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
        }

        return dto;
    }
}