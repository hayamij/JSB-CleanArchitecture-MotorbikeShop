package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.accessory.AddAccessoryInputData;

public interface AddAccessoryInputBoundary {
    void execute(AddAccessoryInputData inputData);
}
