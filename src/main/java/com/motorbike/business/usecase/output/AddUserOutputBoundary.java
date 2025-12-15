package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.adduser.AddUserOutputData;

public interface AddUserOutputBoundary {
    void present(AddUserOutputData outputData);
}