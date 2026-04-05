package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.removeitemfromcart.RemoveItemFromCartInputData;

public interface RemoveItemFromCartInputBoundary {
    void execute(RemoveItemFromCartInputData inputData);
}
