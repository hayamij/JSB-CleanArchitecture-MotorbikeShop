package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.motorbike.ValidateMotorbikeFieldsOutputData;

/**
 * UC-75: Validate Motorbike Specific Fields
 * Output boundary for motorbike field validation
 */
public interface ValidateMotorbikeFieldsOutputBoundary {
    void present(ValidateMotorbikeFieldsOutputData outputData);
}
