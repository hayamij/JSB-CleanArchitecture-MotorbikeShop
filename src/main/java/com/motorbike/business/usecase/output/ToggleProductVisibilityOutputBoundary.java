package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.product.ToggleProductVisibilityOutputData;

public interface ToggleProductVisibilityOutputBoundary {
    void present(ToggleProductVisibilityOutputData outputData);
}
