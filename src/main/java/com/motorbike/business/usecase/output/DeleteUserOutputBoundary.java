package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.deleteuser.DeleteUserOutputData;

public interface DeleteUserOutputBoundary {
    void present(DeleteUserOutputData outputData);
}