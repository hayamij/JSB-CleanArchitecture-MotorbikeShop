package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.calculateproductprice.CalculateProductPriceInputData;

public interface CalculateProductPriceInputBoundary {
    void execute(CalculateProductPriceInputData inputData);
}
