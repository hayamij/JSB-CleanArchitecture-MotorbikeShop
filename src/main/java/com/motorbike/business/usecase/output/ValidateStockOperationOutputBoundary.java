package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.validatestockoperation.ValidateStockOperationOutputData;

public interface ValidateStockOperationOutputBoundary {
    void present(ValidateStockOperationOutputData outputData);
}
