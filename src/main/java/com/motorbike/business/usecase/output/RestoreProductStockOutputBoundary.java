package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.restorestock.RestoreProductStockOutputData;

public interface RestoreProductStockOutputBoundary {
    void present(RestoreProductStockOutputData outputData);
}
