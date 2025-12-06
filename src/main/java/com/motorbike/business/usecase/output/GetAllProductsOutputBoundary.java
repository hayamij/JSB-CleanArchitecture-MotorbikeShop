package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.product.GetAllProductsOutputData;

public interface GetAllProductsOutputBoundary {
    void present(GetAllProductsOutputData outputData);
}
