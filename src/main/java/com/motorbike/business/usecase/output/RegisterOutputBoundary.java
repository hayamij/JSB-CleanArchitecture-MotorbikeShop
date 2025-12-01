package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.register.RegisterOutputData;

public interface RegisterOutputBoundary {
    
    void present(RegisterOutputData outputData);
}
