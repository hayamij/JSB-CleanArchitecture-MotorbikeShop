package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.motorbike.ValidateMotorbikeFieldsInputData;

/**
 * UC-75: Validate Motorbike Specific Fields
 * Input boundary for validating motorbike-specific fields
 */
public interface ValidateMotorbikeFieldsInputBoundary {
    void execute(ValidateMotorbikeFieldsInputData inputData);
}
