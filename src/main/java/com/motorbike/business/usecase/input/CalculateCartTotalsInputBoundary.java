package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.calculatecarttotals.CalculateCartTotalsInputData;

public interface CalculateCartTotalsInputBoundary {
    void execute(CalculateCartTotalsInputData inputData);
}
