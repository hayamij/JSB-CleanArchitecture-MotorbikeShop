package com.motorbike.business.usecase;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;

/**
 * Output boundary for update cart quantity use case.
 * This interface defines how the use case presents results to the outside world.
 */
public interface UpdateCartQuantityOutputBoundary {
    
    /**
     * Presents the result of updating cart item quantity.
     * 
     * @param outputData the output data containing the result of the update operation
     */
    void present(UpdateCartQuantityOutputData outputData);
}
