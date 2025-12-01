package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;

public interface GetProductDetailOutputBoundary {
    void present(GetProductDetailOutputData outputData);
}
