package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData;

public interface GetAllAccessoriesOutputBoundary {
    void present(GetAllAccessoriesOutputData outputData);
}
