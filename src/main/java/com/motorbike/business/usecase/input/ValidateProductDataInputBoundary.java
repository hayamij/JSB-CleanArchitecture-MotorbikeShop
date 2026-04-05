package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.validateproductdata.ValidateProductDataInputData;

public interface ValidateProductDataInputBoundary {
    void execute(ValidateProductDataInputData inputData);
}
