package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.validateproductdata.ValidateProductDataOutputData;

public interface ValidateProductDataOutputBoundary {
    void present(ValidateProductDataOutputData outputData);
}
