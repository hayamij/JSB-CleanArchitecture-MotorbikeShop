package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.applysearchfilters.ApplySearchFiltersInputData;

public interface ApplySearchFiltersInputBoundary {
    void execute(ApplySearchFiltersInputData inputData);
}
