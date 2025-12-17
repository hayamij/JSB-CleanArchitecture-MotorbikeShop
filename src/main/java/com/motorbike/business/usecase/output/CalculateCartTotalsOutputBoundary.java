package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.calculatecarttotals.CalculateCartTotalsOutputData;

public interface CalculateCartTotalsOutputBoundary {
    void present(CalculateCartTotalsOutputData outputData);
}
