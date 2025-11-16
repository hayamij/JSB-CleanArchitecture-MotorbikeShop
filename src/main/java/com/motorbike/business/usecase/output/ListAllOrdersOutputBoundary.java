package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.listallorders.ListAllOrdersOutputData;

public interface ListAllOrdersOutputBoundary {
    void present(ListAllOrdersOutputData outputData);
}