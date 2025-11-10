package com.motorbike.business.repository;

import com.motorbike.business.entity.Cart;

import java.util.Optional;

/**
 * Cart Repository Interface - Business Layer
 * Defines contract for cart data operations
 * Part of Clean Architecture - Business Layer (independent of frameworks)
 */
public interface CartRepository {
    
    /**
     * Find cart by user ID
     * @param userId user ID
     * @return Optional containing cart if found
     */
    Optional<Cart> findByUserId(Long userId);
    
    /**
     * Save or update cart
     * @param cart cart to save
     * @return saved cart
     */
    Cart save(Cart cart);
    
    /**
     * Delete cart by ID
     * @param cartId cart ID
     */
    void deleteById(Long cartId);
    
    /**
     * Check if cart exists for user
     * @param userId user ID
     * @return true if cart exists
     */
    boolean existsByUserId(Long userId);
}
