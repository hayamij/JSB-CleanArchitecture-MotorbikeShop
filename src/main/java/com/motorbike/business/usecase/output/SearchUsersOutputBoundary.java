package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.user.SearchUsersOutputData;

public interface SearchUsersOutputBoundary {
    void present(SearchUsersOutputData outputData);
}
