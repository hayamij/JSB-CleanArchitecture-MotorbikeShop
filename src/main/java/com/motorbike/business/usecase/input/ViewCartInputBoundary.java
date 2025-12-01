package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.viewcart.ViewCartInputData;

public interface ViewCartInputBoundary {
    void execute(ViewCartInputData inputData);
}
