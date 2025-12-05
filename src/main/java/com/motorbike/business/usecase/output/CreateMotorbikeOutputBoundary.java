package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.motorbike.CreateMotorbikeOutputData;

public interface CreateMotorbikeOutputBoundary {
    void present(CreateMotorbikeOutputData outputData);
}
