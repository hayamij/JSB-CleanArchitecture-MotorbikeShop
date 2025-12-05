package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.motorbike.DeleteMotorbikeInputData;

public interface DeleteMotorbikeInputBoundary {
    void execute(DeleteMotorbikeInputData input);
}
