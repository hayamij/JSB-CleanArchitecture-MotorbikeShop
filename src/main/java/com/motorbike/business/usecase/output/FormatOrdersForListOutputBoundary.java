package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.formatordersforlist.FormatOrdersForListOutputData;

/**
 * UC-81: Format Orders For List - Output Boundary
 * Defines the contract for presenting formatted order lists
 */
public interface FormatOrdersForListOutputBoundary {
    void present(FormatOrdersForListOutputData outputData);
}
