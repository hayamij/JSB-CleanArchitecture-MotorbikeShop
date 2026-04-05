package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.hashpassword.HashPasswordOutputData;

public interface HashPasswordOutputBoundary {
    void present(HashPasswordOutputData outputData);
}
