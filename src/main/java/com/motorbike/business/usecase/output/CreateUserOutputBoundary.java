package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.user.CreateUserOutputData;

public interface CreateUserOutputBoundary {
    void present(CreateUserOutputData outputData);
}
