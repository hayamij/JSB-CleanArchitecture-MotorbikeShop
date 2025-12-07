package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.order.UpdateOrderInputData;

public interface UpdateOrderInputBoundary {
    void execute(UpdateOrderInputData inputData);
}
