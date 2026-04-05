package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.user.UpdateUserOutputData;

public interface UpdateUserOutputBoundary {
    void present(UpdateUserOutputData outputData);
}
