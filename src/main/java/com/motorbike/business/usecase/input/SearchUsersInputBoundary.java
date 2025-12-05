package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.user.SearchUsersInputData;

public interface SearchUsersInputBoundary {
    void execute(SearchUsersInputData inputData);
}
