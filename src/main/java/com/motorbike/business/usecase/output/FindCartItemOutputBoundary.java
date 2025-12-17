package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.findcartitem.FindCartItemOutputData;

public interface FindCartItemOutputBoundary {
    
    void present(FindCartItemOutputData outputData);
}
