package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.checkout.CheckoutInputData;

/**
 * Input Boundary Interface: CheckoutInputBoundary
 * Defines the contract for checkout use case
 */
public interface CheckoutInputBoundary {
    void execute(CheckoutInputData inputData);
}
