package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.calculateorderstats.CalculateOrderStatisticsOutputData;

/**
 * UC-80: Calculate Order Statistics - Output Boundary
 * Defines the contract for presenting order statistics
 */
public interface CalculateOrderStatisticsOutputBoundary {
    void present(CalculateOrderStatisticsOutputData outputData);
}
