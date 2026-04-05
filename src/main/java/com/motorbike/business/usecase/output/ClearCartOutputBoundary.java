package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.clearcart.ClearCartOutputData;

public interface ClearCartOutputBoundary {
    void present(ClearCartOutputData outputData);
}
