package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData;

public interface GetAllMotorbikesOutputBoundary {
    void present(GetAllMotorbikesOutputData outputData);
}
