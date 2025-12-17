package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.formatcartitems.FormatCartItemsForDisplayInputData;
import com.motorbike.business.dto.formatcartitems.FormatCartItemsForDisplayOutputData;

/**
 * UC-78: Format Cart Items For Display - Input Boundary
 * Defines the contract for formatting cart items for display
 */
public interface FormatCartItemsForDisplayInputBoundary {
    FormatCartItemsForDisplayOutputData execute(FormatCartItemsForDisplayInputData inputData);
}
