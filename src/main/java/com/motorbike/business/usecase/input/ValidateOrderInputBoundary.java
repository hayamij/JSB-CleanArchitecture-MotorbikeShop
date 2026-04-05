package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.validateorder.ValidateOrderInputData;

public interface ValidateOrderInputBoundary {
    
    void execute(ValidateOrderInputData inputData);
}
