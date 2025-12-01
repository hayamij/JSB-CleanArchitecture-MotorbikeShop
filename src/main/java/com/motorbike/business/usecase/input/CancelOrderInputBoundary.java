package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.cancelorder.CancelOrderInputData;

public interface CancelOrderInputBoundary {
    
    void execute(CancelOrderInputData inputData);
}
