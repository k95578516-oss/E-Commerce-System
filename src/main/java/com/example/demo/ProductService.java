package com.example.demo;

import com.example.demo.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
@Autowired
    private ProductRepository repository;
@Autowired
    private CategoryRepository repo;

    public List<ProductDTO> getAllProducts() {

        List<Product> Products = repository.findAll();

        return Products.stream().map(this::convertToDTO).toList();
    }

    public ProductDTO getProductById(int id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        return convertToDTO(product);
    }

    public ProductDTO saveProduct(ProductDTO dto) {

        Product product = convertToEntity(dto);

        Product savedProduct = repository.save(product);

        return convertToDTO(savedProduct);
    }

    public ProductDTO updateProduct(int id, ProductDTO dto) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(dto.getName());

        Category category = repo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        product.setCategory(category);

        Product updatedProduct = repository.save(product);

        return convertToDTO(updatedProduct);
    }

    public void deleteProduct(int id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        repository.delete(product);
    }

    private ProductDTO convertToDTO(Product Product) {

        ProductDTO dto = new ProductDTO();

        dto.setId(Product.getId());
        dto.setName(Product.getName());
        dto.setPrice(Product.getPrice());
        dto.setDescription(Product.getDescription());
        dto.setStock(Product.getStock());
        dto.setCategoryId(Product.getCategory().getId());

        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {

        Product product = new Product();

        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setStock(dto.getStock());
        Category category = repo.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);
       return product;

    }
    public List<ProductDTO> getProductByCategory(int deptId){
        List<Product> Products = repository.findByCategoryId(deptId);
        return Products.stream().map(this::convertToDTO).toList();
    }
    public List<ProductDTO> searchProducts(String keyword) {

        List<Product> products =
                repository.findByNameContainingIgnoreCase(keyword);

        return products.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Page<ProductDTO> getProduct(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Product> Productpage = repository.findAll(pageable);
        return Productpage.map(this::convertToDTO);
    }


}
