package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.createorder.CreateOrderFromCartOutputData;

public interface CreateOrderFromCartOutputBoundary {
    void present(CreateOrderFromCartOutputData outputData);
}
