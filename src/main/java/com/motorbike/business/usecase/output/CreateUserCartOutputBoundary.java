package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.cart.CreateUserCartOutputData;

public interface CreateUserCartOutputBoundary {
    void present(CreateUserCartOutputData outputData);
}
