package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.motorbike.AddMotorbikeOutputData;

public interface AddMotorbikeOutputBoundary {
    void present(AddMotorbikeOutputData outputData);
}
