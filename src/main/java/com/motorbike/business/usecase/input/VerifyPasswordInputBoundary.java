package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.verifypassword.VerifyPasswordInputData;

public interface VerifyPasswordInputBoundary {
    void execute(VerifyPasswordInputData inputData);
}
