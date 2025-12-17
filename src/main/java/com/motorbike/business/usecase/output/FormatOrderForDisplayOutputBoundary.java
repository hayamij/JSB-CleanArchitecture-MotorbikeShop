package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.formatorderfordisplay.FormatOrderForDisplayOutputData;

public interface FormatOrderForDisplayOutputBoundary {
    void present(FormatOrderForDisplayOutputData outputData);
}
