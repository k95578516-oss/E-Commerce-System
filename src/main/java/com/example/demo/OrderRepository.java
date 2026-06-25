package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);

    @Query("""
       SELECT COALESCE(SUM(o.totalAmount),0)
       FROM Order o
       WHERE o.status <> 'CANCELLED'
       """)
    Double getTotalRevenue();
}