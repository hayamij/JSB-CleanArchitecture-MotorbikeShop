package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.cart.MergeGuestCartInputData;

public interface MergeGuestCartInputBoundary {
    void execute(MergeGuestCartInputData inputData);
}
