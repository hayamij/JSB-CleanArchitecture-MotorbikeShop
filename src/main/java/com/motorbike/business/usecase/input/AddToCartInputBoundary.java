package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.addtocart.AddToCartInputData;

public interface AddToCartInputBoundary {
    
    void execute(AddToCartInputData inputData);
}
