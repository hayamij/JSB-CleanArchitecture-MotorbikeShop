package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.applysearchfilters.ApplySearchFiltersOutputData;

public interface ApplySearchFiltersOutputBoundary {
    void present(ApplySearchFiltersOutputData outputData);
}
