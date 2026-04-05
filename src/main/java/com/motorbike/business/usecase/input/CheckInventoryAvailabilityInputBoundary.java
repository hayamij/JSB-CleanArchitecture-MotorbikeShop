package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.checkinventory.CheckInventoryAvailabilityInputData;

public interface CheckInventoryAvailabilityInputBoundary {
    void execute(CheckInventoryAvailabilityInputData inputData);
}
