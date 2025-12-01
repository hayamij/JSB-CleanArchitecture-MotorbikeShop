package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.register.RegisterInputData;

public interface RegisterInputBoundary {
    
    void execute(RegisterInputData inputData);
}
