package com.motorbike.business.usecase;

import com.motorbike.business.dto.addtocart.AddToCartInputData;

/**
 * Input Boundary (Use Case Interface) for AddToCart
 * Defines the contract for the AddToCart use case
 * Adapter layer depends on this interface
 */
public interface AddToCartInputBoundary {
    /**
     * Execute the add to cart use case
     * @param inputData Contains product ID, quantity, and user/cart info
     */
    void execute(AddToCartInputData inputData);
}
