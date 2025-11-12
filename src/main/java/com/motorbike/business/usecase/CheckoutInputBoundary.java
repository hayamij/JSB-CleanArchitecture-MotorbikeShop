package com.motorbike.business.usecase;

import com.motorbike.business.dto.checkout.CheckoutInputData;

/**
 * Input Boundary Interface: CheckoutInputBoundary
 * Defines the contract for checkout use case
 */
public interface CheckoutInputBoundary {
    void execute(CheckoutInputData inputData);
}
