package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.motorbike.SearchMotorbikesInputData;

public interface SearchMotorbikesInputBoundary {
    void execute(SearchMotorbikesInputData inputData);
}
