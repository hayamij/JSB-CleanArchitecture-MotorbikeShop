package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaOutputData;

public interface BuildSearchCriteriaOutputBoundary {
    void present(BuildSearchCriteriaOutputData outputData);
}
