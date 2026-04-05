package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.calculateproductprice.CalculateProductPriceOutputData;

public interface CalculateProductPriceOutputBoundary {
    void present(CalculateProductPriceOutputData outputData);
}
