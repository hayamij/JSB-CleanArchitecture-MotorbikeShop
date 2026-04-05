package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.order.UpdateOrderOutputData;

public interface UpdateOrderOutputBoundary {
    void present(UpdateOrderOutputData outputData);
}
