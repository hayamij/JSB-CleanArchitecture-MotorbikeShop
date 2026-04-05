package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.formatorderitems.FormatOrderItemsForCheckoutOutputData;

/**
 * UC-82: Format Order Items For Checkout - Output Boundary
 * Defines the contract for presenting formatted order items
 */
public interface FormatOrderItemsForCheckoutOutputBoundary {
    void present(FormatOrderItemsForCheckoutOutputData outputData);
}
