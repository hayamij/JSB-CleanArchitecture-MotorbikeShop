package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.validateuserregistration.ValidateUserRegistrationInputData;

public interface ValidateUserRegistrationInputBoundary {
    void execute(ValidateUserRegistrationInputData inputData);
}
