package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.viewcart.ViewCartOutputData;

/**
 * Output Boundary Interface: ViewCartOutputBoundary
 * Defines the contract for presenting view cart results
 */
public interface ViewCartOutputBoundary {
    void present(ViewCartOutputData outputData);
}
