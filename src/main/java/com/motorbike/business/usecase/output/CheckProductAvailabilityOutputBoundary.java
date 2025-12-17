package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.checkproductavailability.CheckProductAvailabilityOutputData;

public interface CheckProductAvailabilityOutputBoundary {
    
    void present(CheckProductAvailabilityOutputData outputData);
}
