package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.validateuserregistration.ValidateUserRegistrationOutputData;

public interface ValidateUserRegistrationOutputBoundary {
    void present(ValidateUserRegistrationOutputData outputData);
}
