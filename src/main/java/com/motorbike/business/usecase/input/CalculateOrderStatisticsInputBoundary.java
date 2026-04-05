package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.calculateorderstats.CalculateOrderStatisticsInputData;
import com.motorbike.business.dto.calculateorderstats.CalculateOrderStatisticsOutputData;

/**
 * UC-80: Calculate Order Statistics - Input Boundary
 * Defines the contract for calculating order statistics
 */
public interface CalculateOrderStatisticsInputBoundary {
    CalculateOrderStatisticsOutputData execute(CalculateOrderStatisticsInputData inputData);
}
