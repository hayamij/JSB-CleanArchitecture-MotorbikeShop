package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.clearcart.ClearCartInputData;

public interface ClearCartInputBoundary {
    void execute(ClearCartInputData inputData);
}
