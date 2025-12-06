package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.searchadminorder.SearchAdminOrderOutputData;

public interface SearchAdminOrderOutputBoundary {
    void present(SearchAdminOrderOutputData outputData);
}
