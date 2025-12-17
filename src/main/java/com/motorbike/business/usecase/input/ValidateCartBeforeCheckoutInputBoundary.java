package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.validatecart.ValidateCartBeforeCheckoutInputData;

public interface ValidateCartBeforeCheckoutInputBoundary {
    void execute(ValidateCartBeforeCheckoutInputData inputData);
}
