package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.calculateordertotals.CalculateOrderTotalsInputData;

public interface CalculateOrderTotalsInputBoundary {
    void execute(CalculateOrderTotalsInputData inputData);
}
