package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.order.GetValidOrderStatusesInputData;

public interface GetValidOrderStatusesInputBoundary {
    void execute(GetValidOrderStatusesInputData inputData);
}
