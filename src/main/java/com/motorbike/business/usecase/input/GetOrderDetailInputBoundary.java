package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.order.GetOrderDetailInputData;

public interface GetOrderDetailInputBoundary {
    void execute(GetOrderDetailInputData inputData);
}
