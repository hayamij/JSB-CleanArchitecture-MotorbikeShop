package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.sortorders.SortOrdersByDateOutputData;

/**
 * UC-79: Sort Orders By Date - Output Boundary
 * Defines the contract for presenting sorted orders
 */
public interface SortOrdersByDateOutputBoundary {
    void present(SortOrdersByDateOutputData outputData);
}
