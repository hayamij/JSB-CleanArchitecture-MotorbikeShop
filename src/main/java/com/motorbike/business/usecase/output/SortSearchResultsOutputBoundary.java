package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.sortsearchresults.SortSearchResultsOutputData;

public interface SortSearchResultsOutputBoundary {
    void present(SortSearchResultsOutputData outputData);
}
