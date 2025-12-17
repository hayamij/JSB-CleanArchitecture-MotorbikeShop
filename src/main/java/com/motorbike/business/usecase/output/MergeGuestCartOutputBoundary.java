package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.cart.MergeGuestCartOutputData;

public interface MergeGuestCartOutputBoundary {
    void present(MergeGuestCartOutputData outputData);
}
