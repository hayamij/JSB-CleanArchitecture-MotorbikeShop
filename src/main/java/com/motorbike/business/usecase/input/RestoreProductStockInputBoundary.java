package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.restorestock.RestoreProductStockInputData;

public interface RestoreProductStockInputBoundary {
    void execute(RestoreProductStockInputData inputData);
}
