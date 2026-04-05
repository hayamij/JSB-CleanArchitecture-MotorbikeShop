package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.findcartitem.FindCartItemInputData;

public interface FindCartItemInputBoundary {
    
    void execute(FindCartItemInputData inputData);
}
