package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.accessory.UpdateAccessoryOutputData;

public interface UpdateAccessoryOutputBoundary {
    void present(UpdateAccessoryOutputData outputData);
}
