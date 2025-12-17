package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.formatcartitems.FormatCartItemsForDisplayOutputData;

/**
 * UC-78: Format Cart Items For Display - Output Boundary
 * Defines the contract for presenting formatted cart items
 */
public interface FormatCartItemsForDisplayOutputBoundary {
    void present(FormatCartItemsForDisplayOutputData outputData);
}
