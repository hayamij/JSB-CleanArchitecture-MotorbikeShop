package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.validateordercancellation.ValidateOrderCancellationInputData;

public interface ValidateOrderCancellationInputBoundary {
    void execute(ValidateOrderCancellationInputData inputData);
}
