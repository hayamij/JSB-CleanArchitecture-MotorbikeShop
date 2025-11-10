package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.entity.CartItem;
import com.motorbike.business.entity.Product;
import com.motorbike.business.exception.CartItemNotFoundException;
import com.motorbike.business.exception.ProductNotFoundException;
import com.motorbike.business.exception.ProductOutOfStockException;
import com.motorbike.business.repository.CartRepository;
import com.motorbike.business.repository.ProductRepository;
import com.motorbike.business.usecase.UpdateCartQuantityUseCase;

/**
 * UpdateCartQuantityUseCaseImpl - Business Layer
 * Implementation of update cart quantity use case
 * Part of Clean Architecture - Business Layer
 */
public class UpdateCartQuantityUseCaseImpl implements UpdateCartQuantityUseCase {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public UpdateCartQuantityUseCaseImpl(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public UpdateCartQuantityResponse execute(UpdateCartQuantityRequest request) {
        // Step 1: Validate request
        validateRequest(request);
        
        // Step 2: Find cart
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new CartItemNotFoundException(
                        String.format("Cart not found for user %d", request.getUserId())));
        
        // Step 3: Find cart item
        CartItem cartItem = findCartItem(cart, request.getProductId());
        if (cartItem == null) {
            throw new CartItemNotFoundException(request.getUserId(), request.getProductId());
        }
        
        // Step 4: Handle quantity update
        boolean itemRemoved = false;
        String message;
        
        if (request.getNewQuantity() == 0) {
            // Remove item from cart by product ID
            cart.removeItem(request.getProductId());
            itemRemoved = true;
            message = String.format("Product '%s' removed from cart", cartItem.getProductName());
        } else {
            // Update quantity
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));
            
            // Check stock availability
            if (request.getNewQuantity() > product.getStockQuantity()) {
                throw new ProductOutOfStockException(
                        product.getName(),
                        request.getNewQuantity(),
                        product.getStockQuantity()
                );
            }
            
            // Update quantity
            cartItem.updateQuantity(request.getNewQuantity());
            message = String.format("Updated '%s' quantity to %d", 
                    cartItem.getProductName(), request.getNewQuantity());
        }
        
        // Step 5: Save cart (updatedAt is set automatically by removeItem/addItem methods)
        Cart updatedCart = cartRepository.save(cart);
        
        // Step 7: Return response
        return new UpdateCartQuantityResponse(
                updatedCart,
                message,
                itemRemoved,
                true
        );
    }
    
    /**
     * Validate the request
     */
    private void validateRequest(UpdateCartQuantityRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (request.getProductId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (request.getNewQuantity() == null) {
            throw new IllegalArgumentException("New quantity cannot be null");
        }
        if (request.getNewQuantity() < 0) {
            throw new IllegalArgumentException("New quantity cannot be negative");
        }
    }
    
    /**
     * Find cart item by product ID
     */
    private CartItem findCartItem(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }
}
