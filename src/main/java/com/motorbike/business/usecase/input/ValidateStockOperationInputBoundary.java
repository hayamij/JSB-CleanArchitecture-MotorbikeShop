package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.validatestockoperation.ValidateStockOperationInputData;

public interface ValidateStockOperationInputBoundary {
    void execute(ValidateStockOperationInputData inputData);
}
