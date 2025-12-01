package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;

public interface UpdateCartQuantityOutputBoundary {
    
    
    void present(UpdateCartQuantityOutputData outputData);
}
