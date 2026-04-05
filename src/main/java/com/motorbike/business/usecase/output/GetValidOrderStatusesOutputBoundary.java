package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.order.GetValidOrderStatusesOutputData;

public interface GetValidOrderStatusesOutputBoundary {
    void present(GetValidOrderStatusesOutputData outputData);
}
