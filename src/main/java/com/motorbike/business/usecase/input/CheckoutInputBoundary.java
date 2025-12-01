package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.checkout.CheckoutInputData;

public interface CheckoutInputBoundary {
    void execute(CheckoutInputData inputData);
}
