package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.createorder.CreateOrderFromCartInputData;

public interface CreateOrderFromCartInputBoundary {
    void execute(CreateOrderFromCartInputData inputData);
}
