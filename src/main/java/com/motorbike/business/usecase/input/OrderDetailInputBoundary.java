package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.orderdetail.OrderDetailInputData;

public interface OrderDetailInputBoundary {
    void execute(OrderDetailInputData inputData);
}
