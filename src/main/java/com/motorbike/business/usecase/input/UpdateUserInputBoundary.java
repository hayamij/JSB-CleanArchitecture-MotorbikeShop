package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.updateuser.UpdateUserInputData;

public interface UpdateUserInputBoundary {
    void execute(UpdateUserInputData inputData);
}
