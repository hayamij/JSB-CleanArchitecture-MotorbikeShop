package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.reducestock.ReduceProductStockInputData;

public interface ReduceProductStockInputBoundary {
    void execute(ReduceProductStockInputData inputData);
}
