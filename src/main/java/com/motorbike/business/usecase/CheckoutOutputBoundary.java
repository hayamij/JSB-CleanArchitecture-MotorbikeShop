package com.motorbike.business.usecase;

import com.motorbike.business.dto.checkout.CheckoutOutputData;

/**
 * Output Boundary Interface: CheckoutOutputBoundary
 * Defines the contract for presenting checkout results
 */
public interface CheckoutOutputBoundary {
    void present(CheckoutOutputData outputData);
}
