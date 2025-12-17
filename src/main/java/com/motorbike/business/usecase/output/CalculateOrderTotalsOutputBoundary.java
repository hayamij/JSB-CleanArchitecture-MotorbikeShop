package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.calculateordertotals.CalculateOrderTotalsOutputData;

public interface CalculateOrderTotalsOutputBoundary {
    void present(CalculateOrderTotalsOutputData outputData);
}
