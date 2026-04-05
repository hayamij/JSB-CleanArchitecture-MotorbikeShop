package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.removecartitem.RemoveCartItemInputData;

public interface RemoveCartItemInputBoundary {
    
    void execute(RemoveCartItemInputData inputData);
}
