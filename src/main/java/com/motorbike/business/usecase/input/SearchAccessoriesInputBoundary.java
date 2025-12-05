package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;

public interface SearchAccessoriesInputBoundary {
    void execute(SearchAccessoriesInputData inputData);
}
