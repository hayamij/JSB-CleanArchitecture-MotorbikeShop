package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.cancelorder.CancelOrderOutputData;

/**
 * Output Boundary Interface: CancelOrderOutputBoundary
 * Defines the contract for presenting cancel order results
 * Use case depends on this interface (Dependency Inversion)
 */
public interface CancelOrderOutputBoundary {
    void present(CancelOrderOutputData outputData);
}