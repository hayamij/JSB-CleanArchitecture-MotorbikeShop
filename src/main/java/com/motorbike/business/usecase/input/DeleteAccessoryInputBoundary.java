package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.accessory.DeleteAccessoryInputData;

public interface DeleteAccessoryInputBoundary {
    void execute(DeleteAccessoryInputData inputData);
}
