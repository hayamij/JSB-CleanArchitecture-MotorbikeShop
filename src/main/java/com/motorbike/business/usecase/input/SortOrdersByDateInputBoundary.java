package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.sortorders.SortOrdersByDateInputData;
import com.motorbike.business.dto.sortorders.SortOrdersByDateOutputData;

/**
 * UC-79: Sort Orders By Date - Input Boundary
 * Defines the contract for sorting orders by date
 */
public interface SortOrdersByDateInputBoundary {
    SortOrdersByDateOutputData execute(SortOrdersByDateInputData inputData);
}
