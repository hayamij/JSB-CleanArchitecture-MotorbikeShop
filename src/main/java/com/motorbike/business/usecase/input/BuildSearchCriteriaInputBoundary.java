package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaInputData;

public interface BuildSearchCriteriaInputBoundary {
    void execute(BuildSearchCriteriaInputData inputData);
}
