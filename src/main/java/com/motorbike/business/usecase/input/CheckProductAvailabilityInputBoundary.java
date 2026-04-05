package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.checkproductavailability.CheckProductAvailabilityInputData;

public interface CheckProductAvailabilityInputBoundary {
    
    void execute(CheckProductAvailabilityInputData inputData);
}
