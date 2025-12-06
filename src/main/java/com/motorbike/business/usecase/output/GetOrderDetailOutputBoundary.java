package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.order.GetOrderDetailOutputData;

public interface GetOrderDetailOutputBoundary {
    void present(GetOrderDetailOutputData outputData);
}
