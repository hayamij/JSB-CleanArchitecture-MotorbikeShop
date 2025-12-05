package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.listmyorders.ListMyOrdersOutputData;

public interface ListMyOrdersOutputBoundary {
    void present(ListMyOrdersOutputData outputData);
}
