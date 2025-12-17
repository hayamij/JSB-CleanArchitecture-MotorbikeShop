package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.hashpassword.HashPasswordInputData;

public interface HashPasswordInputBoundary {
    void execute(HashPasswordInputData inputData);
}
