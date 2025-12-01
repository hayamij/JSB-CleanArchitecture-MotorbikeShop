package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.login.LoginInputData;

public interface LoginInputBoundary {
    
    void execute(LoginInputData inputData);
}
