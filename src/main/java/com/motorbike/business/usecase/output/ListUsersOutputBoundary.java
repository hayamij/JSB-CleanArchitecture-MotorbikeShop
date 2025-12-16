package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.listusers.ListUsersOutputData;

public interface ListUsersOutputBoundary {
    void present(ListUsersOutputData outputData);
}