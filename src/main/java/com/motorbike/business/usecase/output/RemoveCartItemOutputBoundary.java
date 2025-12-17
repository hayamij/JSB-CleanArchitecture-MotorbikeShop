package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.removecartitem.RemoveCartItemOutputData;

public interface RemoveCartItemOutputBoundary {
    
    void present(RemoveCartItemOutputData outputData);
}
