package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.verifypassword.VerifyPasswordOutputData;

public interface VerifyPasswordOutputBoundary {
    void present(VerifyPasswordOutputData outputData);
}
