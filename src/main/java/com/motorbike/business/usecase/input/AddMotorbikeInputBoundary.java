package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.motorbike.AddMotorbikeInputData;

public interface AddMotorbikeInputBoundary {
    void execute(AddMotorbikeInputData inputData);
}
