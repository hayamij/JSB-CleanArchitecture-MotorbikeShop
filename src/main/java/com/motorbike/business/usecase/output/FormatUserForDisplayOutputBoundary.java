package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.formatuserfordisplay.FormatUserForDisplayOutputData;

public interface FormatUserForDisplayOutputBoundary {
    void present(FormatUserForDisplayOutputData outputData);
}
