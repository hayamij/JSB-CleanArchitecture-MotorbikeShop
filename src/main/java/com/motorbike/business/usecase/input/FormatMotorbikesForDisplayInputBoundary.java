package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.motorbike.FormatMotorbikesForDisplayInputData;

/**
 * UC-74: Format Motorbikes For Display
 * Input boundary for formatting motorbike entities to display items
 */
public interface FormatMotorbikesForDisplayInputBoundary {
    void execute(FormatMotorbikesForDisplayInputData inputData);
}
