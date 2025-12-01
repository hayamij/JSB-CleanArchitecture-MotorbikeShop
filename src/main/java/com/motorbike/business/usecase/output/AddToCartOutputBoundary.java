package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.addtocart.AddToCartOutputData;

public interface AddToCartOutputBoundary {
    
    void present(AddToCartOutputData outputData);
}
