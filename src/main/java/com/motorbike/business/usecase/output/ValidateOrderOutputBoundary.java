package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.validateorder.ValidateOrderOutputData;

public interface ValidateOrderOutputBoundary {
    
    void present(ValidateOrderOutputData outputData);
}
