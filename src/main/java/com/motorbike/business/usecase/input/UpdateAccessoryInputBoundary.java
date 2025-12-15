package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.accessory.UpdateAccessoryInputData;

public interface UpdateAccessoryInputBoundary {
    void execute(UpdateAccessoryInputData inputData);
}
