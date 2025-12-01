package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.checkout.CheckoutOutputData;

public interface CheckoutOutputBoundary {
    void present(CheckoutOutputData outputData);
}
