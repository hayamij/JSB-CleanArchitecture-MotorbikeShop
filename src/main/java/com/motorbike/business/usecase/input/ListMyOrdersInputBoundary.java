package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.listmyorders.ListMyOrdersInputData;

public interface ListMyOrdersInputBoundary {
    void execute(ListMyOrdersInputData inputData);
}
