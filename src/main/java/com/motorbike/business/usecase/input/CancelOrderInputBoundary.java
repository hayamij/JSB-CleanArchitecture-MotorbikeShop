package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.cancelorder.CancelOrderInputData;

/**
 * Input Boundary (Use Case Interface) for Cancel Order
 * Defines the contract for the Cancel Order use case
 * Adapter layer depends on this interface
 */
public interface CancelOrderInputBoundary {
    /**
     * Execute the cancel order use case
     * @param inputData Contains orderId, userId, and cancel reason
     */
    void execute(CancelOrderInputData inputData);
}