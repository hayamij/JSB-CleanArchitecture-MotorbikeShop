package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.user.DeleteUserInputData;

public interface DeleteUserInputBoundary {
    void execute(DeleteUserInputData inputData);
}
