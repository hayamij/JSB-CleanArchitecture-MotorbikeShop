package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.motorbike.DeleteMotorbikeOutputData;

public interface DeleteMotorbikeOutputBoundary {
    void present(DeleteMotorbikeOutputData outputData);
}
