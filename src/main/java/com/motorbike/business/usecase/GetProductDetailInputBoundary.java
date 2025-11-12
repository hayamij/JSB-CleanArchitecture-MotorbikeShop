package com.motorbike.business.usecase;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;

/**
 * Input Boundary (Interface) for Get Product Detail Use Case
 * Defines the entry point to the use case
 */
public interface GetProductDetailInputBoundary {
    void execute(GetProductDetailInputData inputData);
}
