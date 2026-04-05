package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.user.AddUserInputData;

public interface AddUserInputBoundary {
    void execute(AddUserInputData inputData);
}
