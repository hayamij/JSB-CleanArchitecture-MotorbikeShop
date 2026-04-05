package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.topproducts.GetTopProductsOutputData;

public interface GetTopProductsOutputBoundary {
    void present(GetTopProductsOutputData outputData);
}
