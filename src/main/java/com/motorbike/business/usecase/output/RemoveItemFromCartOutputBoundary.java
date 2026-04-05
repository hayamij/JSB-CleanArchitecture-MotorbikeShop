package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.removeitemfromcart.RemoveItemFromCartOutputData;

public interface RemoveItemFromCartOutputBoundary {
    void present(RemoveItemFromCartOutputData outputData);
}
