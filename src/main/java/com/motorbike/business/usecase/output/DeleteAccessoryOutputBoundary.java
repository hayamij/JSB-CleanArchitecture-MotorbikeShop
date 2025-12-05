package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.accessory.DeleteAccessoryOutputData;

public interface DeleteAccessoryOutputBoundary {
    void present(DeleteAccessoryOutputData outputData);
}
