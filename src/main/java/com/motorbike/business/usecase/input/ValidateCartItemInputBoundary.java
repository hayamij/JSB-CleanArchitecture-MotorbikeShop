package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.validatecartitem.ValidateCartItemInputData;

public interface ValidateCartItemInputBoundary {
    
    void execute(ValidateCartItemInputData inputData);
}
