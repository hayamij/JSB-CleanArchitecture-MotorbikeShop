package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.repository.CartRepository;
import com.motorbike.business.usecase.ViewCartUseCase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * View Cart Use Case Implementation - Business Layer
 * Contains the business logic for viewing cart contents
 * Part of Clean Architecture - Business Layer (independent of frameworks)
 */
public class ViewCartUseCaseImpl implements ViewCartUseCase {
    
    private final CartRepository cartRepository;
    
    public ViewCartUseCaseImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    
    @Override
    public ViewCartResponse execute(ViewCartRequest request) {
        // Step 1: Find cart by user ID
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElse(createEmptyCart(request.getUserId()));
        
        // Step 2: Check if cart is empty
        boolean isEmpty = cart.isEmpty();
        
        // Step 3: Prepare message
        String message;
        if (isEmpty) {
            message = "Your cart is empty";
        } else {
            message = String.format(
                "Cart has %d item(s), Total: %s VND",
                cart.getTotalItemCount(),
                cart.getTotalAmount()
            );
        }
        
        // Step 4: Return response
        return new ViewCartResponse(cart, isEmpty, message);
    }
    
    /**
     * Create an empty cart for user
     * @param userId user ID
     * @return empty cart
     */
    private Cart createEmptyCart(Long userId) {
        return Cart.builder()
                .userId(userId)
                .items(new ArrayList<>())
                .totalAmount(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
