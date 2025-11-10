package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.entity.CartItem;
import com.motorbike.business.entity.Product;
import com.motorbike.business.exception.ProductNotFoundException;
import com.motorbike.business.exception.ProductOutOfStockException;
import com.motorbike.business.repository.CartRepository;
import com.motorbike.business.repository.ProductRepository;
import com.motorbike.business.usecase.AddToCartUseCase;

import java.time.LocalDateTime;

/**
 * Add to Cart Use Case Implementation - Business Layer
 * Contains the business logic for adding products to cart
 * Part of Clean Architecture - Business Layer (independent of frameworks)
 */
public class AddToCartUseCaseImpl implements AddToCartUseCase {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public AddToCartUseCaseImpl(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public AddToCartResponse execute(AddToCartRequest request) {
        // Step 1: Validate and fetch product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));
        
        // Step 2: Check if product is available
        if (!product.isAvailable()) {
            throw new ProductOutOfStockException(
                product.getName() + " is not available for purchase"
            );
        }
        
        // Step 3: Check stock quantity
        if (!product.canPurchase(request.getQuantity())) {
            throw new ProductOutOfStockException(
                product.getName(),
                request.getQuantity(),
                product.getStockQuantity()
            );
        }
        
        // Step 4: Get or create cart for user
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElse(Cart.builder()
                        .userId(request.getUserId())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());
        
        // Step 5: Create cart item
        CartItem newItem = CartItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .quantity(request.getQuantity())
                .addedAt(LocalDateTime.now())
                .build();
        
        // Step 6: Add item to cart (will merge if product already exists)
        cart.addItem(newItem);
        
        // Step 7: Save cart
        Cart savedCart = cartRepository.save(cart);
        
        // Step 8: Return success response
        String message = String.format(
            "Added %d x %s to cart successfully",
            request.getQuantity(),
            product.getName()
        );
        
        return new AddToCartResponse(savedCart, message, true);
    }
}
