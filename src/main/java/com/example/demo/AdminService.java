package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public AdminService(UserRepository userRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public DashboardDTO getDashboardStats() {

        long totalUsers = userRepository.count();

        long totalProducts = productRepository.count();

        long totalOrders = orderRepository.count();

        double revenue =
                Optional.ofNullable(orderRepository.getTotalRevenue())
                        .orElse(0.0);

        return new DashboardDTO(
                totalUsers,
                totalProducts,
                totalOrders,
                revenue
        );
    }
}
