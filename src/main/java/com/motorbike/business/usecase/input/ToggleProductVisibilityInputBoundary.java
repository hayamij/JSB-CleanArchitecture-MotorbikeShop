package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.product.ToggleProductVisibilityInputData;

public interface ToggleProductVisibilityInputBoundary {
    void execute(ToggleProductVisibilityInputData inputData);
}
