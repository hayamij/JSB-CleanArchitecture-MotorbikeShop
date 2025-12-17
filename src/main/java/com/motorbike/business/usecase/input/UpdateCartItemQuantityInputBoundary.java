package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.updatecartitemquantity.UpdateCartItemQuantityInputData;

public interface UpdateCartItemQuantityInputBoundary {
    
    void execute(UpdateCartItemQuantityInputData inputData);
}
