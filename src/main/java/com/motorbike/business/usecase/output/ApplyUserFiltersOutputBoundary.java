package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.user.ApplyUserFiltersOutputData;

/**
 * UC-73: Apply User Filters
 * Output boundary for filtering user list
 */
public interface ApplyUserFiltersOutputBoundary {
    void present(ApplyUserFiltersOutputData outputData);
}
