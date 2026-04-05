package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.accessory.CreateAccessoryOutputData;

public interface CreateAccessoryOutputBoundary {
    void present(CreateAccessoryOutputData outputData);
}
