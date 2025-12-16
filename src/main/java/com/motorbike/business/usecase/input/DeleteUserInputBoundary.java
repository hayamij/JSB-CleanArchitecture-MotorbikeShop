package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.deleteuser.DeleteUserInputData;

public interface DeleteUserInputBoundary {
    void execute(DeleteUserInputData inputData);
}
