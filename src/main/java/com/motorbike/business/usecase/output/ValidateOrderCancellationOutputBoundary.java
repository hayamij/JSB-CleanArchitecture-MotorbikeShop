package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.validateordercancellation.ValidateOrderCancellationOutputData;

public interface ValidateOrderCancellationOutputBoundary {
    void present(ValidateOrderCancellationOutputData outputData);
}
