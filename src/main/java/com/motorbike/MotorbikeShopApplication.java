package com.motorbike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class for Motorbike Shop
 * Clean Architecture với Spring Boot
 * 
 * Server chạy tại: http://localhost:8080
 * 
 * API Endpoints:
 * 
 * Authentication APIs:
 * - POST   /api/auth/register      - Đăng ký tài khoản mới
 * - POST   /api/auth/login         - Đăng nhập
 * 
 * Product APIs:
 * - GET    /api/products/{id}      - Xem chi tiết sản phẩm
 * 
 * Cart APIs:
 * - POST   /api/cart/add           - Thêm sản phẩm vào giỏ hàng
 * - GET    /api/cart/{userId}      - Xem giỏ hàng
 * - PUT    /api/cart/update        - Cập nhật số lượng sản phẩm trong giỏ
 * 
 * Order APIs:
 * - POST   /api/orders/checkout    - Thanh toán đơn hàng
 */
@SpringBootApplication
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
