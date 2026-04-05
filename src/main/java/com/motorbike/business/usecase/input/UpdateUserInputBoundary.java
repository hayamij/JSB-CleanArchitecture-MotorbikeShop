package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.user.UpdateUserInputData;

public interface UpdateUserInputBoundary {
    void execute(UpdateUserInputData inputData);
}
