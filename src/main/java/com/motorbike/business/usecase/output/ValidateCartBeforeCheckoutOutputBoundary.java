package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.validatecart.ValidateCartBeforeCheckoutOutputData;

public interface ValidateCartBeforeCheckoutOutputBoundary {
    void present(ValidateCartBeforeCheckoutOutputData outputData);
}
