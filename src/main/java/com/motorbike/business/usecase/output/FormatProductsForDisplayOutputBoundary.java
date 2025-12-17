package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.product.FormatProductsForDisplayOutputData;

public interface FormatProductsForDisplayOutputBoundary {
    void present(FormatProductsForDisplayOutputData outputData);
}
