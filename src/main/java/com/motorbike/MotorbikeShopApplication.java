package com.motorbike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.motorbike.infrastructure.persistence.jpa.repositories")
public class MotorbikeShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MotorbikeShopApplication.class, args);
        System.out.println("Server: http://localhost:8080");
        System.out.println("API Documentation:");
        System.out.println("Auth:    /api/auth/*");
        System.out.println("Product: /api/products/*");
        System.out.println("Cart:    /api/cart/*");
        System.out.println("Order:   /api/orders/*");
    }
}
