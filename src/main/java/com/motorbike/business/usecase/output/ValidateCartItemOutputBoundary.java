package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.validatecartitem.ValidateCartItemOutputData;

public interface ValidateCartItemOutputBoundary {
    
    void present(ValidateCartItemOutputData outputData);
}
