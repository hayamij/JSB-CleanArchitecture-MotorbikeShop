package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.user.ApplyUserFiltersInputData;

/**
 * UC-73: Apply User Filters
 * Input boundary for filtering user list
 */
public interface ApplyUserFiltersInputBoundary {
    void execute(ApplyUserFiltersInputData inputData);
}
