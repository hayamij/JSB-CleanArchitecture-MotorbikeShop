package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.updateorderstatus.UpdateOrderStatusOutputData;

public interface UpdateOrderStatusOutputBoundary {
    void present(UpdateOrderStatusOutputData outputData);
}
