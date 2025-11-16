package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;

public interface ListAllOrdersInputBoundary {
    void execute(ListAllOrdersInputData inputData);
}