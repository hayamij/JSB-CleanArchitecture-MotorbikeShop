package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;

/**
 * Output Boundary (Interface) for Get Product Detail Use Case
 * Defines the exit point from the use case to the presenter
 */
public interface GetProductDetailOutputBoundary {
    void present(GetProductDetailOutputData outputData);
}
