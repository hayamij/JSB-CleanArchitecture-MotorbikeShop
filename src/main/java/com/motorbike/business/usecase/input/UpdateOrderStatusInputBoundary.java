package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.updateorderstatus.UpdateOrderStatusInputData;

public interface UpdateOrderStatusInputBoundary {
    void execute(UpdateOrderStatusInputData inputData);
}
