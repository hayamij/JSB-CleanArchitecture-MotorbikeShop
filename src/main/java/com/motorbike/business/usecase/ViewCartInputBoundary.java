package com.motorbike.business.usecase;

import com.motorbike.business.dto.viewcart.ViewCartInputData;

/**
 * Input Boundary Interface: ViewCartInputBoundary
 * Defines the contract for view cart use case
 */
public interface ViewCartInputBoundary {
    void execute(ViewCartInputData inputData);
}
