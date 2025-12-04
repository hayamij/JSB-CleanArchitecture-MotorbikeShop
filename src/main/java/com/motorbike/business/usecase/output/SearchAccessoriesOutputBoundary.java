package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData;

public interface SearchAccessoriesOutputBoundary {
    void present(SearchAccessoriesOutputData outputData);
}
