package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.cart.CreateUserCartInputData;

public interface CreateUserCartInputBoundary {
    void execute(CreateUserCartInputData inputData);
}
