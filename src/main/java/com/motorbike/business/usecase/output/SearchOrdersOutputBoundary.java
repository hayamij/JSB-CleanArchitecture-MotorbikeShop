package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.order.SearchOrdersOutputData;

public interface SearchOrdersOutputBoundary {
    void present(SearchOrdersOutputData outputData);
}
