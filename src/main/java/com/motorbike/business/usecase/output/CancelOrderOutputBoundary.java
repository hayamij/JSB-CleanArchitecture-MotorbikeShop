package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.cancelorder.CancelOrderOutputData;

public interface CancelOrderOutputBoundary {
    void present(CancelOrderOutputData outputData);
}
