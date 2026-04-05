package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.reducestock.ReduceProductStockOutputData;

public interface ReduceProductStockOutputBoundary {
    void present(ReduceProductStockOutputData outputData);
}
