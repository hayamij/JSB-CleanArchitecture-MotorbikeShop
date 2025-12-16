package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.adduser.AddUserInputData;

public interface AddUserInputBoundary {
    void execute(AddUserInputData inputData);
}
