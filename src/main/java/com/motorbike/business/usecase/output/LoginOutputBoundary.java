package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.login.LoginOutputData;

public interface LoginOutputBoundary {
    
    void present(LoginOutputData outputData);
}
