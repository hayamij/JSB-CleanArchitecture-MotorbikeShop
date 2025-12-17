package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.checkinventory.CheckInventoryAvailabilityOutputData;

public interface CheckInventoryAvailabilityOutputBoundary {
    void present(CheckInventoryAvailabilityOutputData outputData);
}
