package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.formatproductfordisplay.FormatProductForDisplayInputData;

public interface FormatProductForDisplayInputBoundary {
    void execute(FormatProductForDisplayInputData inputData);
}
