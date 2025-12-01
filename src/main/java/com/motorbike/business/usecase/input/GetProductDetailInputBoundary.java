package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;

public interface GetProductDetailInputBoundary {
    void execute(GetProductDetailInputData inputData);
}
