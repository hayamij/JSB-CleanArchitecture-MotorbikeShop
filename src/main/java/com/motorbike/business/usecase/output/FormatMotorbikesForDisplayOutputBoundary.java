package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.motorbike.FormatMotorbikesForDisplayOutputData;

/**
 * UC-74: Format Motorbikes For Display
 * Output boundary for formatting motorbike list
 */
public interface FormatMotorbikesForDisplayOutputBoundary {
    void present(FormatMotorbikesForDisplayOutputData outputData);
}
