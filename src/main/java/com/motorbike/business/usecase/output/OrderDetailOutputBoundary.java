package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.orderdetail.OrderDetailOutputData;

public interface OrderDetailOutputBoundary {
    void present(OrderDetailOutputData outputData);
}
