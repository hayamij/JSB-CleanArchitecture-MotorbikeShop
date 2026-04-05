package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.updatecartitemquantity.UpdateCartItemQuantityOutputData;

public interface UpdateCartItemQuantityOutputBoundary {
    
    void present(UpdateCartItemQuantityOutputData outputData);
}
