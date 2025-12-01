package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;

public interface UpdateCartQuantityInputBoundary {
    
    
    void execute(UpdateCartQuantityInputData inputData);
}
