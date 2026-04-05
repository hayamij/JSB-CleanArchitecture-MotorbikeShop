package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.user.DeleteUserOutputData;

public interface DeleteUserOutputBoundary {
    void present(DeleteUserOutputData outputData);
}
