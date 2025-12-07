package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.searchadminorder.SearchAdminOrderInputData;

public interface SearchAdminOrderInputBoundary {
    void execute(SearchAdminOrderInputData inputData);
}
