package com.motorbike.business.usecase;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;

/**
 * Input boundary for updating cart item quantity use case.
 * This interface defines the contract for updating the quantity of a product in the cart.
 */
public interface UpdateCartQuantityInputBoundary {
    
    /**
     * Executes the update cart quantity use case.
     * 
     * @param inputData the input data containing user/cart identification and update details
     */
    void execute(UpdateCartQuantityInputData inputData);
}
