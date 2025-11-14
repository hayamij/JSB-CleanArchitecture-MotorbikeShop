package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.addtocart.AddToCartOutputData;

/**
 * Output Boundary (Presenter Interface) for AddToCart
 * Defines the contract for presenting add to cart results
 * Use case depends on this interface (Dependency Inversion)
 */
public interface AddToCartOutputBoundary {
    /**
     * Present the add to cart result
     * @param outputData Contains cart data or error information
     */
    void present(AddToCartOutputData outputData);
}
