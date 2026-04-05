package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.formatordersforlist.FormatOrdersForListInputData;
import com.motorbike.business.dto.formatordersforlist.FormatOrdersForListOutputData;

/**
 * UC-81: Format Orders For List - Input Boundary
 * Defines the contract for formatting orders for list display
 */
public interface FormatOrdersForListInputBoundary {
    FormatOrdersForListOutputData execute(FormatOrdersForListInputData inputData);
}
