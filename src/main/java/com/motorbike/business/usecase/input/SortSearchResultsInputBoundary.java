package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.sortsearchresults.SortSearchResultsInputData;

public interface SortSearchResultsInputBoundary {
    void execute(SortSearchResultsInputData inputData);
}
