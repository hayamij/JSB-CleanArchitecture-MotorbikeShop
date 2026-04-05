package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.motorbike.UpdateMotorbikeInputData;

public interface UpdateMotorbikeInputBoundary {
    void execute(UpdateMotorbikeInputData inputData);
}
