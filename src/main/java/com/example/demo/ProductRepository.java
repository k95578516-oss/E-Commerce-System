package com.example.demo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Integer> {

    List<Product> findByCategoryIdAndDeletedFalse(int categoryId);

    List<Product> findByNameContainingIgnoreCaseAndDeletedFalse(
            String keyword);

    List<Product> findByDeletedFalse();

    Optional<Product> findByIdAndDeletedFalse(int id);

    Page<Product> findByDeletedFalse(Pageable pageable);
}