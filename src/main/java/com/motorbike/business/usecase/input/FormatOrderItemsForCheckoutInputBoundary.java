package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.formatorderitems.FormatOrderItemsForCheckoutInputData;
import com.motorbike.business.dto.formatorderitems.FormatOrderItemsForCheckoutOutputData;

/**
 * UC-82: Format Order Items For Checkout - Input Boundary
 * Defines the contract for formatting order items for checkout response
 */
public interface FormatOrderItemsForCheckoutInputBoundary {
    FormatOrderItemsForCheckoutOutputData execute(FormatOrderItemsForCheckoutInputData inputData);
}
