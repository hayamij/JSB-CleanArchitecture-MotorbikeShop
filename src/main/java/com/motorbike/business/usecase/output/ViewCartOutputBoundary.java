package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.viewcart.ViewCartOutputData;

public interface ViewCartOutputBoundary {
    void present(ViewCartOutputData outputData);
}
