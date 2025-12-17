package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.formatproductfordisplay.FormatProductForDisplayOutputData;

public interface FormatProductForDisplayOutputBoundary {
    void present(FormatProductForDisplayOutputData outputData);
}
