package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.motorbike.UpdateMotorbikeOutputData;

public interface UpdateMotorbikeOutputBoundary {
    void present(UpdateMotorbikeOutputData outputData);
}
