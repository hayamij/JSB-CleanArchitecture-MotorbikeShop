package com.motorbike.adapters.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to serve HTML pages.
 * Uses @Controller (not @RestController) to return views.
 */
@Controller
public class WebViewController {

    /**
     * Root path - redirect to index page
     */
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    /**
     * Home page
     */
    @GetMapping("/home")
    public String home() {
        return "forward:/home.html";
    }

    /**
     * Login page
     */
    @GetMapping("/login")
    public String login() {
        return "forward:/login.html";
    }

    /**
     * Register page
     */
    @GetMapping("/register")
    public String register() {
        return "forward:/register.html";
    }

    /**
     * Product detail page
     */
    @GetMapping("/product-detail")
    public String productDetail() {
        return "forward:/product-detail.html";
    }

    /**
     * Cart page (requires authentication)
     */
    @GetMapping("/cart")
    public String cart() {
        return "forward:/cart.html";
    }

    /**
     * Checkout page (requires authentication)
     */
    @GetMapping("/checkout")
    public String checkout() {
        return "forward:/checkout.html";
    }

    /**
     * My orders page (requires authentication)
     */
    @GetMapping("/my-orders")
    public String myOrders() {
        return "forward:/my-orders.html";
    }

    /**
     * Admin dashboard (requires ADMIN role)
     */
    @GetMapping("/admin")
    public String admin() {
        return "forward:/admin.html";
    }

    /**
     * Admin users page (requires ADMIN role)
     */
    @GetMapping("/admin/users")
    public String adminUsers() {
        return "forward:/admin-users.html";
    }

    /**
     * Admin products page (requires ADMIN role)
     */
    @GetMapping("/admin/products")
    public String adminProducts() {
        return "forward:/admin-products.html";
    }

    /**
     * Admin orders page (requires ADMIN role)
     */
    @GetMapping("/admin/orders")
    public String adminOrders() {
        return "forward:/admin-orders.html";
    }
}
