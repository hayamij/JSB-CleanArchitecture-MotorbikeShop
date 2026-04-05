package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.user.GetAllUsersOutputData;

public interface GetAllUsersOutputBoundary {
    void present(GetAllUsersOutputData outputData);
}
